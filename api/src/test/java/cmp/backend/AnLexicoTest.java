package cmp.backend;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class AnLexicoTest {

    @Test
    public void testIFconComentario() {

        //Cadena a evaluar
        String str = "IF ( contador==1.1){return=1;} /* Comentario bloque */ // Comentario de línea \n Else {return=-5;}";
        AnLexico scanner = new AnLexico(str,1);
        List<String> tokens = scanner.AnalizadorCadena();
        
        // Tokens esperados
        String[] expectedTokens = {
            "IF   -> Reservada",
            "(   -> Agrupador",
            "contador   -> Identificador",
            "==   -> Operador",
            "1.1   -> NumReal",
            ")   -> Agrupador",
            "{   -> Agrupador",
            "return   -> Reservada",
            "=   -> Operador",
            "1   -> NumEntero",
            ";   -> Terminal",
            "}   -> Agrupador",
            "Else   -> Reservada",
            "{   -> Agrupador",
            "return   -> Reservada",
            "=   -> Operador",
            "-5   -> NumEntero",
            ";   -> Terminal",
            "}   -> Agrupador"
        };
        
        assertEquals(expectedTokens.length, tokens.size());
        for (int i = 0; i < expectedTokens.length; i++) {
            assertEquals(expectedTokens[i], tokens.get(i));
        }
    }
    @Test
    void testDeclaracionVariables() {
        // Cadena a evaluar
        String str = "Entero i, j, k; ";
        AnLexico scanner = new AnLexico(str, 1);
        List<String> tokens = scanner.AnalizadorCadena();

        // Tokens esperados
        String[] expectedTokens = {
            "Entero   -> Reservada",
            "i   -> Identificador",
            ",   -> Separador",
            "j   -> Identificador",
            ",   -> Separador",
            "k   -> Identificador",
            ";   -> Terminal"
        };

        assertEquals(expectedTokens.length, tokens.size());
        for (int i = 0; i < expectedTokens.length; i++) {
            assertEquals(expectedTokens[i], tokens.get(i));
        }
    }
}