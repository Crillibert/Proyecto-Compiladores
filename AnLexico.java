import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;;
public class AnLexico {
    //Los comentarios pueden ser aceptados con // y terminan con salto de linea
    // pueden ir con /* y */ si son entre varias lineas
    //Manejar: entero, real, bool, char y string
    //[tipo de dato] [identificador][,identificador]*[=<expresión>];
    //Ejemplo
    /*Entero intVar;
    Entero i, j, k;
    Entero i = 1+1;*/
    //Se debe de declarar el tipo de variable antes del nombre
    //# es modulo

    //Regex para evaluar cadenas
    Pattern PatronNombreVariable = Pattern.compile("[A-z]+");
    Pattern PatronNumero = Pattern.compile("(-|)([0-9]+|([0-9]+.[0-9]+))");
    Pattern PatronTipoDeDato = Pattern.compile("(Real)|(Entero)|(Booleano)|(Caracter)|(Cadena)");
    //Este solo revisa para operadores de dos caracteres
    Pattern PatronOperadores = Pattern.compile("[+][+]|[-][-]|[*][*]|[<][=]|[>][=]|[=][=]");
    Pattern PatronesReservadas = Pattern.compile("(if)|(else)|(then)|(return)|(for)|(do)|(while)|(EscribirLinea)|(Escribir)|(Longitud)|(aCadena)");
    //private String[] lexemas = new String[]{"**", "==", ">=", "<>", "<="};
    //Agregar operador ++ y --
    private char[] operadores = new char[]{'+', '-', '*', '/', '^', '=', '#', '>', '<'};
    private char[] agrupadores = new char[]{'(', ')', '[', ']', '{', '}', '"'};
    private List<String> Tokens = new ArrayList<>();
    private String CadenaFuente;

    //Constructor
    public AnLexico(String pEntrada){
        this.CadenaFuente = pEntrada;
    }


    //Basado en el original del inge pero con regex en lugar de listas

    private boolean esOperadorChar(Character pCharacter){
        for (Character c: operadores){
            if (c.equals(pCharacter)){
                return true;
            }
        }
        return false;
    }
    private boolean esAgrupadorChar(Character pCharacter){
        for (Character c: agrupadores){
            if (c.equals(pCharacter)){
                return true;
            }
        }
        return false;
    }
    private boolean esReservada(String pString){
        Matcher MatchReservada = PatronesReservadas.matcher(pString);
        if (MatchReservada.matches()) {
            return true;            
        }
        return false;
    }
    private boolean esTipoDeDato(String pString){
        Matcher MatchTipoDeDato = PatronTipoDeDato.matcher(pString);
        if (MatchTipoDeDato.matches()) {
            return true;            
        }
        return false;
    }
    private boolean esIdentificador(String pString){
        Matcher MatchVariable = PatronNombreVariable.matcher(pString);
        if (MatchVariable.matches()) {
            return true;            
        }
        return false;
    }
    private boolean esOperador(String pString){
        Matcher MatchOperador = PatronOperadores.matcher(pString);
        if (MatchOperador.matches()) {
            return true;            
        }
        return false;
    }
    private boolean esVariable(String pString){
        Matcher MatchVariable = PatronNombreVariable.matcher(pString);
        if (MatchVariable.matches()){
            return true;
        }
        return false;
    }
    private boolean esNumero(String pString){
        Matcher MatchNumero = PatronNumero.matcher(pString);
        if (MatchNumero.matches()){
            return true;
        }
        return false;
    }

    public List<String> AnalizadorCadena(){
        char[] fuente = CadenaFuente.toCharArray();
        String lexema = "";
        int i = 0;

        //La prioridad deberia ir 
        //Reservada/Tipo de dato -> Operador -> Agrupador(?) -> Variable/Numero

        for (Character c : fuente){
            
            char siguiente = 'ø';
            try {
                siguiente = fuente[i+1];
            } catch (Exception e) {
                //ø es un caracter que (ojala) nunca se leera normalmente, entonces sirve para errores
                siguiente = 'ø';
                //siguiente = fuente[i];
            }
            //TODO: Cambiar la logica aca para poder analizar con agrupadores, tal vez un bool para ver si siguiente cumple o con operador o agrupador
            if (Character.isWhitespace(c) || (esOperador(Character.toString(siguiente)) && lexema != "")){
                //TODO: Introducir la logica y jerarquia para reservadas/variables/numeros
                if (lexema.length()>0){

                }


            }
            else if (c == ';'){
                Tokens.add(Character.toString(c) + "   -> Terminal");
            }
            else if (esOperadorChar(c)){
                String StringOperador = Character.toString(c);
                if (esOperadorChar(siguiente)){
                    String CombOpSig = StringOperador + Character.toString(siguiente);
                    if (esOperador(CombOpSig)){
                        Tokens.add(CombOpSig + "    -> Operador");
                    }
                    else {
                        Tokens.add(StringOperador + "    -> Operador");
                    }
                }
                else {
                    Tokens.add(StringOperador + "   -> Operador");
                }
            }
            else if (esAgrupadorChar(c)){
                Tokens.add(Character.toString(c) + "   -> Agrupador");
            }
            else if ((esNumero(lexema) && !Character.isDigit(siguiente)) ){
                Tokens.add(lexema +"   -> NumEntero");
                lexema = "";
                lexema += c;
            }
            else if (esReservada(lexema.toLowerCase()) || esTipoDeDato(lexema.toLowerCase())){
                Tokens.add(lexema + "   -> Reservada");
                lexema = "";
                lexema += c;
            }
            else if (!lexema.isEmpty() && !esIdentificador(lexema+c) && esIdentificador(lexema)){
                Tokens.add(lexema +"   -> Identificador");
                lexema = "";
                lexema += c;

            }
            else {
                lexema += Character.toString(c);               
            }
            i++;
        }
        return Tokens;
    }
    public static void main(String[] args) {
        String str = "IF ( contador==1){return=1;} Else {return=0;} \r\n";
        AnLexico scanner  =  new AnLexico(str);    
        List<String> Tokens = scanner.AnalizadorCadena();
        for (String s : Tokens){
            System.out.println(s);
        }
    }
}

