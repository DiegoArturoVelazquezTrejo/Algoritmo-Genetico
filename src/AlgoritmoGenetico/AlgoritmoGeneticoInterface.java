package AlgoritmoGenetico;
import java.util.*;

public interface AlgoritmoGeneticoInterface {

    //Variables
    //Lista/ que contendra a los elementos de la población.
    public ArrayList<String> poblacion = new ArrayList<>();

    //Lista/ que contendrá las medidas fit de los elementos de la población.
    public ArrayList<Double> medidasFit = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------
    //Cuerpo del Algoritmo Genético

    //Metodo decodificador de cada elemento de la población
    public Double decodificacion(String elemento);

    //Este metodo tiene que ir añadiendo a la lista la medida fit dependiendo el elemento del índice en el que se encuentre.
    public Double funcionObjetivo(Double rp);

    public void asignarMedidaFit();

    //Este metodo tiene que crear una lista y añadir a esa lista los dos padres que haya seleccionado.
    public ArrayList<String> bernouliSeleccion();


    //Métodos para el cruzamiento de los padres

    public String crossOverPuntoCorte(List<String> padres);

    public String crossOverProbabilistico(List<String> padres);

    public String crossOverVolado(List<String> padres);

    public String seleccinarMetodoCruza(List<String> padres);


    //Método para mutar obteniendo un elemento del adn del elemento
    public char randomChoice();

    //Metodo para la mutación del hijo
    public String mutacion(String hijo);


    //Cuerpo del Algoritmo Genetico
    public void evolucionar();

}
