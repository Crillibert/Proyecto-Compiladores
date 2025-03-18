package cmp.backend;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Hashtable;

public class AnLexico {
    // Los comentarios pueden ser aceptados con // y terminan con salto de linea
    // Pueden ir con /* y */ si son entre varias lineas
    // Manejar: entero, real, bool, char y string
    //[tipo de dato] [identificador][,identificador]*[=<expresión>];
    // Ejemplo
    /* Entero intVar;
    Entero i, j, k;
    Entero i = 1+1;*/
    // Se debe de declarar el tipo de variable antes del nombre
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
    private int numeroLinea;

    // Constructor
    public AnLexico(String pEntrada, int pLinea){
        this.CadenaFuente = pEntrada;
        this.numeroLinea = pLinea;
    }

    // Basado en el original del inge pero con regex en lugar de listas

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
        return MatchReservada.matches();
    }

    private boolean esTipoDeDato(String pString){
        Matcher MatchTipoDeDato = PatronTipoDeDato.matcher(pString);
        return MatchTipoDeDato.matches();
    }
    private boolean esIdentificador(String pString){
        Matcher MatchVariable = PatronNombreVariable.matcher(pString);
        return MatchVariable.matches();
    }

    private boolean esOperador(String pString){
        Matcher MatchOperador = PatronOperadores.matcher(pString);
        return MatchOperador.matches();
    }

    private boolean esNumero(String pString){
        Matcher MatchNumero = PatronNumero.matcher(pString);
        return MatchNumero.matches();
    }

    public List<String> AnalizadorCadena() {
        int id = 0;
        String idString;
        Dictionary<String, String> TablaDeSimbolos = new Hashtable<>();
        char[] fuente = CadenaFuente.toCharArray();
        String lexema = "";
        int i = 0;
        boolean enComentarioBloque = false;
        boolean enComentarioLinea = false;

        while (i < fuente.length) {
            char c = fuente[i];
            char siguiente = (i + 1 < fuente.length) ? fuente[i + 1] : 'ε';
            
            // Detectar apertura de comentario de bloque "/*"
            if (!enComentarioLinea && !enComentarioBloque && c == '/' && siguiente == '*') {
                enComentarioBloque = true;
                i += 2; // Saltar "/*"
                continue;
            }
            
            // Detectar cierre de comentario de bloque "*/"
            if (enComentarioBloque && c == '*' && siguiente == '/') {
                enComentarioBloque = false;
                i += 2; // Saltar "*/"
                continue;
            }
            
            // Detectar comentario de línea "//"
            if (!enComentarioLinea && !enComentarioBloque && c == '/' && siguiente == '/') {
                enComentarioLinea = true;
                i += 2; // Saltar "//"
                continue;
            }

            // Si estamos en un comentario de línea, ignoramos hasta el salto de línea
            if (enComentarioLinea) {
                if (c == '\n') {
                    enComentarioLinea = false;
                }
                i++;
                continue;
            }
            // Si estamos dentro de un comentario de bloque, ignoramos todo
            if (enComentarioBloque) {
                i++;
                continue;
            }

            // Si es un espacio en blanco o un delimitador, procesar el lexema acumulado
            if (Character.isWhitespace(c) || esOperadorChar(c) || esAgrupadorChar(c) || c == ';') {
                if (!lexema.isEmpty()) {
                    // Clasificar el lexema acumulado
                    if (esReservada(lexema.toLowerCase()) || esTipoDeDato(lexema.toLowerCase())) {
                        Tokens.add(lexema + "   -> Reservada");
                        TablaDeSimbolos.put(lexema, "Reservada");
                    } else if (esNumero(lexema)) {
                        if (lexema.contains(".")) {// Distinción para real o entero
                            Tokens.add(lexema + "   -> NumReal");
                        } else {
                            Tokens.add(lexema + "   -> NumEntero");
                        }
                        id++;
                        idString = "ID:" + Integer.toString(id);
                        TablaDeSimbolos.put(lexema, idString);

                    } else if (esIdentificador(lexema)) {
                        Tokens.add(lexema + "   -> Identificador");
                        id++;
                        idString = "ID:" + Integer.toString(id);
                        TablaDeSimbolos.put(lexema, idString);
                    }else{
                        Tokens.add(lexema + "   -> Error en columna "+i+", fila" +numeroLinea);
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
                                TablaDeSimbolos.put(operadorCombinado, "Operador");
                                i++; // Saltar el siguiente carácter ya que es parte del operador combinado
                            } else {
                                Tokens.add(operador + "   -> Operador");
                                TablaDeSimbolos.put(operador, "Operador");
                            }
                        } else {
                            Tokens.add(operador + "   -> Operador");
                            TablaDeSimbolos.put(operador, "Operador");
                        }
                    }
                } else if (esAgrupadorChar(c)) {
                    Tokens.add(Character.toString(c) + "   -> Agrupador");
                    TablaDeSimbolos.put(Character.toString(c), "Agrupador");
                } else if (c == ';') {
                    Tokens.add(Character.toString(c) + "   -> Terminal");
                    TablaDeSimbolos.put(Character.toString(c), "Terminal");
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
                id++;
                idString = "ID:" + Integer.toString(id);
                TablaDeSimbolos.put(lexema, idString);
                
            } else if (esIdentificador(lexema)) {
                Tokens.add(lexema + "   -> Identificador");
                id++;
                idString = "ID:" + Integer.toString(id);
                TablaDeSimbolos.put(lexema, idString);
            }
        }
        return Tokens;
    }

    public static void main(String[] args) {
        String str = "IF ( contador==1.1){return=1;} /* Comentario bloque */ // Comentario de línea \n Else {return=-5;}";
        AnLexico scanner = new AnLexico(str,1);    
        List<String> Tokens = scanner.AnalizadorCadena();
        for (String s : Tokens){
            System.out.println(s);
        }
    }
}
