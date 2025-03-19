package cmp.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;

@RestController
@RequestMapping("/analizador")
@CrossOrigin(origins = "http://localhost:3000") // Permite que cualquier frontend se conecte
public class Controller {

    @PostMapping("/analizar")
    public ResponseEntity<?> analizarCodigo(@RequestBody Map<String, String> body) {
        String codigo = body.get("codigo");
        
        // Validación simple
        if (codigo == null || codigo.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "No se recibió código a analizar"));
        }

        // Aquí llamás a tu clase AnLexico (necesitás adaptar este ejemplo si tu AnLexico funciona por línea)
        AnLexico analizador = new AnLexico(codigo, 1); // Si tu constructor necesita la línea y número de línea
        Map.Entry<List<String>, List<String>> resultado = analizador.AnalizadorCadena();

    // Extrae las listas de Tokens y Errors
    List<String> tokens = resultado.getKey();
    List<String> errors = resultado.getValue();

    // Devuelve ambas listas en la respuesta
    Map<String, List<String>> response = new HashMap<>();
    response.put("tokens", tokens);
    response.put("errors", errors);

    return ResponseEntity.ok(response);
    }
}
