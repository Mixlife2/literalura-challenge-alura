package com.alura.literalura.controller;

import com.alura.literalura.model.Autor;
import com.alura.literalura.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AuthorService authorService;

    // Endpoint para listar autores vivos en un año específico
    @GetMapping("/vivos/{year}")
    public List<Autor> obtenerAutoresVivosEnAnio(@PathVariable Integer year) {
        try {
            // Validar que el año sea razonable (opcional)
            if (year < 1000 || year > 3000) {
                throw new IllegalArgumentException("El año ingresado es inválido");
            }

            // Llamar al servicio para obtener autores vivos en el año proporcionado
            return authorService.obtenerAutoresVivosEnAnio(year);
        } catch (Exception e) {
            // Manejo de errores si el año es inválido o hay algún otro problema
            throw new RuntimeException("Error al obtener autores vivos en el año: " + e.getMessage());
        }
    }
}
