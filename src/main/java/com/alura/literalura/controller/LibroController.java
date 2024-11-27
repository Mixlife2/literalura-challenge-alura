package com.alura.literalura.controller;

import com.alura.literalura.model.Libro;
import com.alura.literalura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private BookService bookService;

    @PostMapping("/crearDesdeApi")
    public Libro crearLibroDesdeApi(@RequestParam String titulo) {
        try {
            // Llamar al servicio para crear y guardar el libro
            return bookService.crearLibroDesdeApi(titulo);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el libro: " + e.getMessage());
        }
    }

    @GetMapping("/cantidadPorIdioma/{idioma}")
    public long obtenerCantidadDeLibrosPorIdioma(@PathVariable String idioma) {
        return bookService.obtenerCantidadDeLibrosPorIdioma(idioma);
    }
}
