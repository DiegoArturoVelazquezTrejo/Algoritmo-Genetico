package AlgoritmoGenetico;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AlgoritmoGeneticoImplementacion implements AlgoritmoGeneticoInterface {


    private ArrayList<String> poblacion = new ArrayList<>();
    private ArrayList<String> poblacionHijos = new ArrayList<>();
    private ArrayList<Double> arrayMedidasFit = new ArrayList<>();

    private Double coeficienteMutacion;
    private String metodoCruza = "CrossOverProbabilistico";
    private int numeroGeneraciones;

    private Double temperatura1;
    private Double temperatura3;
    private Double alfa;
    private Double cp;


    public AlgoritmoGeneticoImplementacion(ArrayList<String> poblacion, Double coeficienteMutacion, String metodoCruza, int numeroGeneraciones, VariablesTermicas variables){

        this.poblacion = poblacion;
        this.coeficienteMutacion = coeficienteMutacion;
        this.metodoCruza = metodoCruza;
        this.numeroGeneraciones = numeroGeneraciones;

        //Variables respectivas a la función objetivo
        this.temperatura1 = variables.getTemperatura1();
        this.temperatura3 = variables.getTemperatura3();
        this.alfa = variables.getAlfa();
        this.cp = variables.getCp();


    }

    public Double decodificacion(String elemento) {
        Double numero = 0.0;
        int var = 0;
        for(int i = 0 ; i < 5; i++){
            var = (int) elemento.charAt(i) - 48;
            numero = numero + var * Math.pow(2, (i));
        }
        for(int i = 5 ; i < elemento.length(); i++){
            var = (int) elemento.charAt(i) - 48;
            numero = numero + var * Math.pow(2, -(i));
        }

        /*    La decodificación del elemento se basa en:
        *   Los primeros cinco numeros representan el 2 elevado a la potencia n multiplicado por el elemento.
        *   Los últimos cuatro numeros de la cadena serán los números decimales que se sumarán.
        */
        return numero;
    }

    @Override
    public Double funcionObjetivo(Double rp) {

        //Codificación de la función objetivo para maximizar el trabajo en un ciclo termodinámico.
        Double trabajo = 0.0;


        Double exponente1 = (1 - this.alfa)/this.alfa;
        Double exponente2 = (this.alfa - 1)/this.alfa;
        Double rpPrimero = Math.pow(rp, exponente1);
        Double rpSegundo = Math.pow(rp, exponente2);

        Double coefTemp = this.temperatura3 / this.temperatura1;


        trabajo = (this.cp * this.temperatura1) * ((coefTemp * rpPrimero) - 1 + rpSegundo - coefTemp);


        return trabajo;


    }

    @Override
    public void asignarMedidaFit() {
        ArrayList<Double> medidasProvisionales = new ArrayList<>();
        this.arrayMedidasFit.clear();
        double suma = 0.0;
        for(int i = 0; i < this.poblacion.size(); i++){
            Double valor = this.decodificacion(this.poblacion.get(i));
            medidasProvisionales.add(this.funcionObjetivo(valor));
            suma = suma + medidasProvisionales.get(i);
        }
        for(int i = 0; i < this.poblacion.size(); i++){
            this.arrayMedidasFit.add(medidasProvisionales.get(i) * 100 / suma);
        }

        return;
    }

    @Override
    public ArrayList<String> bernouliSeleccion() {
        ArrayList<String> padres = new ArrayList<>();
        Random numerosA = new Random();
        int i = 0;
        while( i != 2){
            int padre = numerosA.nextInt(this.poblacion.size());
            Double random = Math.random() * 100;
            if(random < this.arrayMedidasFit.get(padre)) {
                padres.add(this.poblacion.get(padre));
                i = i + 1;
            }
        }
        return padres;
    }

    @Override
    public String crossOverPuntoCorte(List<String> padres) {

        int puntoMedio = Integer.valueOf(padres.get(0).length() / 2);
        String adn1 = padres.get(0).substring(0,puntoMedio);
        String adn2 = padres.get(1).substring(puntoMedio,padres.get(0).length());
        return adn1+adn2;
    }

    @Override
    public String crossOverProbabilistico(List<String> padres) {

        Random numerosA = new Random();
        int puntoCorte = numerosA.nextInt(padres.get(0).length());
        String adn1 = padres.get(0).substring(0,puntoCorte);
        String adn2 = padres.get(1).substring(puntoCorte,padres.get(0).length());
        return adn1 + adn2;
    }

    @Override
    public String crossOverVolado(List<String> padres) {

        String hijo = "";
        Double random;
        for(int i = 0; i < padres.get(0).length(); i++){
            random = Math.random();
            if(random < 0.5){
                hijo = hijo + padres.get(0).charAt(i);
            }else{
                hijo = hijo + padres.get(1).charAt(i);
            }
        }
        return hijo;
    }

    @Override
    public String seleccinarMetodoCruza(List<String> padres) {
        switch (this.metodoCruza){
            case "crossOverVolado":
                return this.crossOverVolado(padres);
            case "crossOverProbabilistico":
                return this.crossOverProbabilistico(padres);
            case "crossOverPuntoCorte":
                return this.crossOverPuntoCorte(padres);
        }
        return null;
    }

    @Override
    public char randomChoice(){
        char opcion;
        Double randomAleatorio = Math.random();
        if(randomAleatorio < 0.5)
            opcion = '0';
        else
            opcion = '1';
        return opcion;
    }

    @Override
    public String mutacion(String hijo) {
        String hijo_mutado = "";
        Double numeroAleatorio;
        for(int i = 0; i < hijo.length(); i++){
            numeroAleatorio = Math.random();
            if(numeroAleatorio < this.coeficienteMutacion)
                hijo_mutado = hijo_mutado + this.randomChoice();
            else
                hijo_mutado = hijo_mutado + hijo.charAt(i);
        }
        return hijo_mutado;
    }

    @Override
    public void evolucionar() {

        for(int i = 0; i < this.numeroGeneraciones; i++){
            this.asignarMedidaFit();
            ArrayList<String> poblacionConHijos = new ArrayList<>();
            for(int u = 0; u < this.poblacion.size(); u++){
                ArrayList<String> padres = this.bernouliSeleccion();
                String hijo = this.seleccinarMetodoCruza(padres);
                hijo = this.mutacion(hijo);
                poblacionConHijos.add(hijo);
            }
            this.poblacion.clear();
            this.poblacion = poblacionConHijos;

        }
    }


    //Métodos getters
    public ArrayList<String> getPoblacion(){
        return this.poblacion;
    }

    public ArrayList<Double> getArrayMedidasFit(){
        return this.arrayMedidasFit;
    }

    public ArrayList<Double> getPoblacionDecodificada(){
        ArrayList<Double> poblacionDecodificada = new ArrayList<>();
        for(int i = 0; i < this.poblacion.size(); i++){
            poblacionDecodificada.add(this.decodificacion(this.poblacion.get(i)));
        }
        return poblacionDecodificada;
    }

    public ArrayList<Double> getEvaluacionPoblacion(){
        ArrayList<Double> evaluacionElementos = new ArrayList<>();
        for(int i = 0; i < this.poblacion.size(); i++){
            evaluacionElementos.add(this.funcionObjetivo(this.decodificacion(this.poblacion.get(i))));
        }
        return evaluacionElementos;
    }

    public HashMap<Double, Double> mostrarInformacionFinal(){
        HashMap<Double, Double> variables = new HashMap<>();

        for(int i = 0; i < this.poblacion.size(); i++){
            String elemento = this.poblacion.get(i);
            System.out.println(elemento+ " Equivale a " + this.decodificacion(elemento) + " Con valor en función de: " + this.funcionObjetivo(this.decodificacion(elemento)));
            variables.put(this.decodificacion(elemento), this.funcionObjetivo(this.decodificacion(elemento)));
        }

        return variables;
    }

}
