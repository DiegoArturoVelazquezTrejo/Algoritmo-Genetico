package AlgoritmoGenetico;

public class VariablesTermicas {

    private Double temperatura1, temperatura3, alfa, cp;
    public VariablesTermicas(Double temperatura1, Double temperatura3, Double alfa, Double cp){
        //DEfinida por el usuario, temperatura1.
        this.temperatura1 = temperatura1;
        //Definida por el usuario, temperatura3.
        this.temperatura3 = temperatura3;
        //Definida por el usuario, alfa es la variable K en los gases o mejor Cp/Cv.
        this.alfa = alfa;
        //DEfinida por el usuario con base en cada gas, es la capacidad calorífica.
        this.cp = cp;
    }

    //Getters de la clase
    public Double getTemperatura1(){
        return temperatura1;
    }
    public Double getTemperatura3(){
        return temperatura3;
    }
    public Double getAlfa(){
        return alfa;
    }
    public Double getCp(){
        return cp;
    }

    //Setters de la clase
    public void setTemperatura1(Double temperatura1){
        this.temperatura1 = temperatura1;
    }
    public void setTemperatura3(Double temperatura3){
        this.temperatura3 = temperatura3;
    }
    public void setAlfa(Double alfa){
        this.alfa = alfa;
    }
    public void setCp(Double cp){
        this.cp = cp;
    }

}
