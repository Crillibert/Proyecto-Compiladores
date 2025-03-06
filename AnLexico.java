import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class AnLexico {
    // Expresiones regulares para evaluar cadenas
    Pattern PatronNombreVariable = Pattern.compile("[A-z]+");
    Pattern PatronNumero = Pattern.compile("(-|)([0-9]+|([0-9]+.[0-9]+))");
    Pattern PatronTipoDeDato = Pattern.compile("(Real)|(Entero)|(Booleano)|(Caracter)|(Cadena)");
    Pattern PatronOperadores = Pattern.compile("[+][+]|[-][-]|[*][*]|[<][=]|[>][=]|[=][=]");
    Pattern PatronesReservadas = Pattern.compile("(if)|(else)|(then)|(return)|(for)|(do)|(while)|(EscribirLinea)|(Escribir)|(Longitud)|(aCadena)");
    
    private char[] operadores = new char[]{'+', '-', '*', '/', '^', '=', '#', '>', '<'};
    private char[] agrupadores = new char[]{'(', ')', '[', ']', '{', '}', '"'};
    private List<String> Tokens = new ArrayList<>();
    private String CadenaFuente;

    // Constructor
    public AnLexico(String pEntrada){
        this.CadenaFuente = pEntrada;
    }

    public List<String> AnalizadorCadena() {
        char[] fuente = CadenaFuente.toCharArray();
        String lexema = "";
        int i = 0;
        boolean enComentario = false;
        
        while (i < fuente.length) {
            char c = fuente[i];
            char siguiente = (i + 1 < fuente.length) ? fuente[i + 1] : 'ø';
            
            // Detectar apertura de comentario "/*"
            if (!enComentario && c == '/' && siguiente == '*') {
                enComentario = true;
                i += 2; // Saltar "/*"
                continue;
            }
            
            // Detectar cierre de comentario "*/"
            if (enComentario && c == '*' && siguiente == '/') {
                enComentario = false;
                i += 2; // Saltar "*/"
                continue;
            }
            
            // Si estamos en un comentario, ignoramos todo
            if (enComentario) {
                i++;
                continue;
            }
            
            // Si es un espacio en blanco o un delimitador, procesar el lexema acumulado
            if (Character.isWhitespace(c) || esOperadorChar(c) || esAgrupadorChar(c) || c == ';') {
                if (!lexema.isEmpty()) {
                    // Clasificar el lexema acumulado
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
                    lexema = "";
                }
                
                // Procesar el carácter actual
                if (esOperadorChar(c)) {
                    String operador = Character.toString(c);
                    if (esOperadorChar(siguiente)) {
                        String operadorCombinado = operador + Character.toString(siguiente);
                        if (esOperador(operadorCombinado)) {
                            Tokens.add(operadorCombinado + "   -> Operador");
                            i++; 
                        } else {
                            Tokens.add(operador + "   -> Operador");
                        }
                    } else {
                        Tokens.add(operador + "   -> Operador");
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

    private boolean esOperadorChar(Character c){
        for (Character op: operadores) {
            if (op.equals(c)) return true;
        }
        return false;
    }

    private boolean esAgrupadorChar(Character c){
        for (Character ag: agrupadores) {
            if (ag.equals(c)) return true;
        }
        return false;
    }

    private boolean esReservada(String s){
        return PatronesReservadas.matcher(s).matches();
    }

    private boolean esTipoDeDato(String s){
        return PatronTipoDeDato.matcher(s).matches();
    }

    private boolean esIdentificador(String s){
        return PatronNombreVariable.matcher(s).matches();
    }

    private boolean esOperador(String s){
        return PatronOperadores.matcher(s).matches();
    }

    private boolean esNumero(String s){
        return PatronNumero.matcher(s).matches();
    }

    public static void main(String[] args) {
        String str = "IF ( contador==1.1){return=1;} /* Esto es un comentario */ Else {return=-5;}";
        AnLexico scanner  =  new AnLexico(str);
        List<String> Tokens = scanner.AnalizadorCadena();
        for (String s : Tokens){
            System.out.println(s);
        }
    }
}
