package cmp.backend;

import java.util.regex.Pattern;

public class GestiondeErrores {
    Pattern PatronErroresNumeros = Pattern.compile(
    "(\\d+\\.\\d+\\.\\d+|\\d+\\.\\D|\\.\\d+|\\d+\\.|[^0-9.]\\d+|\\d+[^0-9.])"
    );
    Pattern PatronErroresTipoDeDato = Pattern.compile(
        "[a-z]*(Reall|Enteroo|Booleann|Carcter|Cadema)[a-z]*",
        Pattern.CASE_INSENSITIVE
    );
    Pattern PatronErroresOperadores = Pattern.compile(
    "[<>=!\\-*\\/+\\^&|a-z]*([+][*]|[*][+]|[-][*]|[=][+]|[!][=][=]|[<][<]|[>][>])[<>=!\\-*\\/+\\^&|a-z]*"
    );
    Pattern PatronErroresReservadas = Pattern.compile(
        "[a-z]*(iff|els|thenn|retrun|foor|whyle|escribirlinea|escrebir|longtud|aCadema)[a-z]*",
        Pattern.CASE_INSENSITIVE
    );
    private String Token = "";
    public GestiondeErrores (String error ){
        this.Token = error;
    }
    public String detectarError() {
        if (PatronErroresNumeros.matcher(Token).matches()) {
            return Token + "    -> Error numérico inválido    ";
        }
        if (PatronErroresTipoDeDato.matcher(Token).matches()) {
            return Token + "    -> Tipo de dato inválido    ";
        }
        if (PatronErroresOperadores.matcher(Token).matches()) {
            return Token + "    -> Operador inválido    ";
        }
        if (PatronErroresReservadas.matcher(Token).matches()) {
            return Token + "    -> Palabra reservada inválida   ";
        }
        return Token + " -> Error desconocido   ";
    }
}
