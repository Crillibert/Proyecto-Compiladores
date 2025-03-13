package cmp.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
public class LectorDeArchivos {
    //Se crea una lista de listas, donde cada lista son los tokens analizados
    private String nombreArchivo;
    public LectorDeArchivos(String pNombre){
        this.nombreArchivo = pNombre;
    }
    public List<List<String>> LeerArchivo(){
        List<List<String>> ListaFinal = new ArrayList<List<String>>();
        try {
            File Archivo = new File(nombreArchivo);
            Scanner Lector = new Scanner(Archivo);
            int numeroLinea = 1;
            while (Lector.hasNextLine()){
                String Linea = Lector.nextLine();
                AnLexico scanner  =  new AnLexico(Linea,numeroLinea); 
                List<String> Tokens = scanner.AnalizadorCadena();
                ListaFinal.add(Tokens);
                numeroLinea++;
            }
            Lector.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error, no se encontro el archivo.");
            e.printStackTrace();
           /*  List<String> listaError = new ArrayList<>();
            listaError.add("ø");
            ListaFinal.add(listaError);*/
        }
        return ListaFinal;
    }
    //Al implementar el front end, esta parte se va
    //Se puede usar de ejemplo de como funcionara
    public static void main(String[] args) {
        LectorDeArchivos scanner = new LectorDeArchivos("filename.txt");
        List<List<String>> Tokens = scanner.LeerArchivo();
        for (List<String> s : Tokens){
            if(!s.isEmpty()){
                System.out.println(s.get(0));
                for(int i = 1; i < s.size(); i++){
                    System.out.println(s.get(i));
                }
            }
            System.out.println("________________________");
        }
    }
    
}
