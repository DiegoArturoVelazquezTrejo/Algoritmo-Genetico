package StringSources;
import java.util.ArrayList;
import java.util.List;
public class StringOperations {

    public static void main(String[] args) {

        String esquema = "****";
        StringOperations string_poblacion = new StringOperations(esquema);
        int tamaño_poblacion = (int) Math.pow(2, string_poblacion.count('*'));
        List<String> poblacion_provsional = string_poblacion.createPoblacion(string_poblacion.numeros_binarios(tamaño_poblacion));
        System.out.println(poblacion_provsional);
    }

    String string;

    public StringOperations(String string){
        this.string = string;
    }
    public int count(char stringChar){
        int count = 0;
        for(int i = 0; i < this.string.length(); i++){
            if (this.string.charAt(i) == stringChar) {
                count++;
            }
        }
        return count;
    }
    public List<String> numeros_binarios(int num){

        int count_asteriscos = (int) (Math.log10(num)/Math.log10(2));
        //Esta variable nos ayudará para darle formato a todas las cadenas y que tengan la misma longitud.
        String format_regrex = "%0"+String.valueOf(count_asteriscos)+"d";
        List<String> list_Numeros = new ArrayList<>();
        for(int i = 0; i < num; i++){
            String nume = String.format(format_regrex,Integer.valueOf(Integer.toBinaryString(i)));
            list_Numeros.add(nume);
        }
        return list_Numeros;
    }

    //Te regresa los índices en donde están los asteriscos
    public List<Integer> indexesOf(char find){
        int count_num = this.count('*');
        List<Integer> indexes = new ArrayList<>();
        for(int i = 0; i < this.string.length(); i++){
            if(this.string.charAt(i) == find){
                indexes.add(i);
            }
        }
        return indexes;
    }
    public List<String> createPoblacion(List<String> numeros_crudos){

        List<String> lista_Combinaciones = new ArrayList<>();
        List<Integer> indexes = this.indexesOf('*');
        String new_String;


        for(int i = 0; i < numeros_crudos.size(); i++){
            new_String = this.string;
            char[] array_element = new_String.toCharArray();
            for(int u = 0; u < indexes.size(); u++) {
                array_element[indexes.get(u)] = numeros_crudos.get(i).charAt(u);
            }
            lista_Combinaciones.add(String.valueOf(array_element));
        }
        return lista_Combinaciones;
    }

}
