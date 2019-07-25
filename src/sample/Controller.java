package sample;
import AlgoritmoGenetico.VariablesTermicas;
import StringSources.StringOperations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import AlgoritmoGenetico.AlgoritmoGeneticoImplementacion;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    ObservableList<String> metodosCruza = FXCollections.observableArrayList("crossOverPuntoCorte", "crossOverProbabilistico", "crossOverVolado");

    Random numerosA = new Random();


    @FXML private AreaChart<?, ?> graficaInicial;

    @FXML private TextField coeficienteMutacion;

    @FXML private ListView<?> valorFXOptimizado;

    @FXML private ChoiceBox<String> metodoCruza;

    @FXML private AreaChart<?, ?> graficaOptimizada;

    @FXML private ListView<?> valorDecodificadoInicial;

    @FXML private ListView<?> elementosOptimizados;

    @FXML private ListView<?> valorDecodificadoOptimizado;

    @FXML private TextField numeroGeneraciones;

    @FXML private ListView<?> valorFXInicial;

    @FXML private ListView<?> elementosIniciales;

    @FXML private TextField temperatura3Input;

    @FXML private TextField temperatura1Input;

    @FXML private TextField cpInput;

    @FXML private TextField Kinput;

    @FXML private ListView<?> eficienciaInicial;

    @FXML private ListView<?> eficienciaOptimizada;



    public void ejecutarAlgoritmo(){

        System.out.println("Ejecutando Algoritmo Genético");


        //Variables del Algoritmo Genético.
        int generaciones = Integer.valueOf(numeroGeneraciones.getText());
        String metodoCruzamiento = metodoCruza.getValue();
        Double mutacionRate = Double.valueOf(coeficienteMutacion.getText());

        //Variables térmicas.
        Double temperatura1 = Double.valueOf(temperatura1Input.getText());
        Double temperatura3 = Double.valueOf(temperatura3Input.getText());
        Double alfa = Double.valueOf(Kinput.getText());
        Double cp = Double.valueOf(cpInput.getText());

        //Serán 9 bits para codificar el dominio de la funcion para la relación de temperaturas.
        ArrayList<String> poblacion = crearPoblacion(9);

        //Este objeto tendrá todas las varuables termidinpamicas necesarias para ejecutar la función objetivo del algoritmo genético.
        VariablesTermicas variables = new VariablesTermicas(temperatura1, temperatura3, alfa, cp);


        //Creando el objeto de tipo Algoritmo Genético, cuerpo  de todo el programa.
        AlgoritmoGeneticoImplementacion AG = new AlgoritmoGeneticoImplementacion(poblacion, mutacionRate, metodoCruzamiento, generaciones, variables);


        colocarInformacionInicial(AG);


        AG.evolucionar();

        colocarInformacionFinal(AG);


    }


    public ArrayList<String> crearPoblacion(int largoBitsPoblacion){
        String esquema = "";
        for(int i = 0; i < largoBitsPoblacion; i++){
            esquema = esquema + "*";
        }

        StringOperations string_poblacion = new StringOperations(esquema);
        int tamaño_poblacion = (int) Math.pow(2, string_poblacion.count('*'));
        List<String> poblacion_provsional = string_poblacion.createPoblacion(string_poblacion.numeros_binarios(tamaño_poblacion));


        ArrayList<String> poblacion = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            int elemento = numerosA.nextInt(poblacion_provsional.size()-1);
            poblacion.add(poblacion_provsional.get(elemento));
        }

        return poblacion;
    }
    public Double funcion_Objetivo_Matematica(Double valor){
        Double parte1 = (1 - Math.pow((11/2 * valor - 7/2),2));
        Double parte2 = (Math.cos(11/2 * valor - 7/2) + 1);
        return (parte1 * parte2) + 2;
    }
    public Double funcionEficiencia(Double valor){
        Double alfa = Double.valueOf(Kinput.getText());
        Double exponente = (alfa - 1) / alfa;
        return (1 - (1 / Math.pow(valor, exponente))) * 100;
    }


    public void colocarInformacionInicial(AlgoritmoGeneticoImplementacion obj){

        ObservableList poblacion = FXCollections.observableArrayList(obj.getPoblacion());
        ObservableList elementosDecodificados = FXCollections.observableArrayList(obj.getPoblacionDecodificada());
        ObservableList elementosMedidos = FXCollections.observableArrayList(obj.getEvaluacionPoblacion());

        ArrayList<Double> eficiencias = new ArrayList<>();
        for(int i = 0; i < elementosDecodificados.size(); i++){
            eficiencias.add(funcionEficiencia((Double) elementosDecodificados.get(i)));
        }

        ObservableList eficienciasIniciales = FXCollections.observableArrayList(eficiencias);


        valorFXInicial.getItems().setAll(elementosMedidos);
        valorDecodificadoInicial.getItems().setAll(elementosDecodificados);
        elementosIniciales.getItems().setAll(poblacion);
        eficienciaInicial.getItems().setAll(eficienciasIniciales);


        graficarPuntos(eficienciasIniciales, elementosMedidos, graficaInicial);

    }
    public void colocarInformacionFinal(AlgoritmoGeneticoImplementacion obj){

        ObservableList poblacion = FXCollections.observableArrayList(obj.getPoblacion());
        ObservableList elementosDecodificados = FXCollections.observableArrayList(obj.getPoblacionDecodificada());
        ObservableList elementosMedidos = FXCollections.observableArrayList(obj.getEvaluacionPoblacion());

        ArrayList<Double> eficiencias = new ArrayList<>();
        for(int i = 0; i < elementosDecodificados.size(); i++){
            eficiencias.add(funcionEficiencia((Double) elementosDecodificados.get(i)));
        }

        ObservableList eficienciasIniciales = FXCollections.observableArrayList(eficiencias);

        eficienciaOptimizada.getItems().setAll(eficienciasIniciales);

        valorFXOptimizado.getItems().setAll(elementosMedidos);
        valorDecodificadoOptimizado.getItems().setAll(elementosDecodificados);
        elementosOptimizados.getItems().setAll(poblacion);

        graficarPuntos(eficienciasIniciales, elementosMedidos, graficaOptimizada);
    }
    public void graficarPuntos(ObservableList medidasEnX, ObservableList medidasEvaluadas, AreaChart<?,?> grafica){

        XYChart.Series series = new XYChart.Series();
        grafica.getData().remove(0,grafica.getData().size());
        for(int i = 0; i < medidasEnX.size(); i++){
            series.getData().add(new XYChart.Data(String.valueOf(medidasEnX.get(i)), medidasEvaluadas.get(i)));
        }
        grafica.getData().addAll(series);
    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        metodoCruza.getItems().setAll(metodosCruza);
        metodoCruza.setValue("crossOverProbabilistico");
        coeficienteMutacion.setText("0.350");
    }
}
