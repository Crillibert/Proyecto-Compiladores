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
    Pattern PatronNombreVariable = Pattern.compile("[A-z]+|[A-z]+[,]");
    Pattern PatronNumero = Pattern.compile("(-|)([0-9]+|([0-9]+.[0-9]+))");
    Pattern PatronTipoDeDato = Pattern.compile("(Real)|(Entero)|(Booleano)|(Caracter)|(Cadena)");
    //Este solo revisa para operadores de dos caracteres
    Pattern PatronOperadores = Pattern.compile("[+][+]|[-][-]|[*][*]|[<][=]|[>][=]|[=][=]|[!][=]|[|][|]|[&][&]");
    Pattern PatronesReservadas = Pattern.compile("(if)|(else)|(then)|(return)|(for)|(do)|(while)|(EscribirLinea)|(Escribir)|(Longitud)|(aCadena)");
    //private String[] lexemas = new String[]{"**", "==", ">=", "<>", "<="};
    //Agregar operador ++ y --
    private char[] operadores = new char[]{'+', '-', '*', '/', '^', '=', '#', '>', '<','#','!'};
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
    private boolean esComentario(String pString){
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
     private boolean esNumero(String pString){
        Matcher MatchNumero = PatronNumero.matcher(pString);
        if (MatchNumero.matches()){
            return true;
        }
        return false;
    }

    public List<String> AnalizadorCadena() {
        char[] fuente = CadenaFuente.toCharArray();
        String lexema = "";
        int i = 0;
    
        while (i < fuente.length) {
            char c = fuente[i];
            char siguiente = (i + 1 < fuente.length) ? fuente[i + 1] : 'ε';
    
            // Si es un espacio en blanco o un delimitador, procesar el lexema acumulado
            if (Character.isWhitespace(c) || esOperadorChar(c) || esAgrupadorChar(c) || c == ';') {
                if (!lexema.isEmpty()) {
                    // Clasificar el lexema acumulado
                    if (esReservada(lexema.toLowerCase()) || esTipoDeDato(lexema.toLowerCase())) {
                        Tokens.add(lexema + "   -> Reservada");
                    } else if (esNumero(lexema)) {
                        if (lexema.contains(".")) {// Distinción para real o entero
                            Tokens.add(lexema + "   -> NumReal");
                        } else {
                            Tokens.add(lexema + "   -> NumEntero");
                        }
                    } else if (esIdentificador(lexema)) {
                        Tokens.add(lexema + "   -> Identificador");
                    }
                    lexema = ""; // Reiniciar el lexema
                }
    
                if (esOperadorChar(c)) {
                    // Manejar el caso especial del signo negativo
                    if (c == '-' && Character.isDigit(siguiente)) {
                        // Si es un signo negativo seguido de un dígito, acumularlo en el lexema
                        lexema += c;
                    } else {
                        String operador = Character.toString(c);
                        if (esOperadorChar(siguiente)) {
                            String operadorCombinado = operador + Character.toString(siguiente);
                            if (esOperador(operadorCombinado)) {
                                Tokens.add(operadorCombinado + "   -> Operador");
                                i++; // Saltar el siguiente carácter ya que es parte del operador combinado
                            } else {
                                Tokens.add(operador + "   -> Operador");
                            }
                        } else {
                            Tokens.add(operador + "   -> Operador");
                        }
                    }
                } else if (esAgrupadorChar(c)) {
                    Tokens.add(Character.toString(c) + "   -> Agrupador");
                } else if (c == ';') {
                    Tokens.add(Character.toString(c) + "   -> Terminal");
                }
            } else {
                // Acumular caracteres en el lexema
                lexema += c;
            }
            i++;
        }
    
        // Procesar el último lexema si queda algo pendiente
        if (!lexema.isEmpty()) {
            if (esReservada(lexema.toLowerCase()) || esTipoDeDato(lexema.toLowerCase())) {
                Tokens.add(lexema + "   -> Reservada");
            } else if (esNumero(lexema)) {
                if (lexema.contains(".")) {
                    Tokens.add(lexema + "   -> NumReal");
                } else {
                    Tokens.add(lexema + "   -> NumEntero");
                }
            } else if (esIdentificador(lexema)) {
                Tokens.add(lexema + "   -> Identificador");
            }
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