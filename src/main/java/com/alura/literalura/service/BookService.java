package com.alura.literalura.service;

import com.alura.literalura.model.DatosAutor;
import com.alura.literalura.model.DatosLibros;
import com.alura.literalura.model.Libro;
import com.alura.literalura.model.Autor;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.repository.AutorRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final String API_URL = "https://gutendex.com/books/";

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public Libro crearLibro(DatosLibros datosLibro) {
        // Crear libro
        Libro libro = new Libro();
        libro.setTitulo(datosLibro.titulo());
        libro.setIdioma(datosLibro.idioma());
        libro.setNumeroDeDescargas(datosLibro.numeroDeDescargas());

        // Crear autores y asociarlos al libro
        List<Autor> autores = datosLibro.autor().stream().map(datosAutor -> {
            Autor autor = new Autor();
            autor.setNombre(datosAutor.nombre());
            autor.setFechaDeNacimiento(datosAutor.fechaDeNacimiento());
            autor.setFechaDeFallecimiento(datosAutor.fechaDeFallecimiento());
            return autorRepository.save(autor); // Guardar autor en la base de datos
        }).collect(Collectors.toList());

        libro.setAutores(autores);

        // Guardar libro en la base de datos
        return libroRepository.save(libro);
    }

    public Libro crearLibroDesdeApi(String titulo) throws IOException {
        // Paso 1: Buscar el libro desde la API externa
        DatosLibros datosLibro = buscarLibroPorTitulo(titulo);

        // Paso 2: Crear libro y guardarlo en la base de datos
        return crearLibro(datosLibro);
    }



    public DatosLibros buscarLibroPorTitulo(String titulo) throws IOException {
        // Construir la URL dinámica con el parámetro de búsqueda
        String urlConTitulo = API_URL + "?search=" + titulo.replace(" ", "+");
        URL url = new URL(urlConTitulo);

        // Realizar la solicitud HTTP
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Leer y mapear la respuesta JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(connection.getInputStream());
        JsonNode resultados = rootNode.get("results");

        if (resultados == null || !resultados.isArray() || resultados.size() == 0) {
            throw new RuntimeException("No se encontraron libros con el título: " + titulo);
        }

        // Tomar el primer resultado
        JsonNode primerLibro = resultados.get(0);

        // Mapear los datos del libro
        return mapearDatosLibro(primerLibro);
    }

    private DatosLibros mapearDatosLibro(JsonNode libroNode) {
        String titulo = libroNode.get("title").asText();
        String idioma = libroNode.get("languages").get(0).asText();
        int descargas = libroNode.get("download_count").asInt();

        // Crear lista de DatosAutor
        List<DatosAutor> autores = new ArrayList<>();
        JsonNode authorsNode = libroNode.get("authors");
        if (authorsNode != null && authorsNode.isArray()) {
            for (JsonNode authorNode : authorsNode) {
                String nombre = authorNode.get("name").asText();
                String fechaDeNacimiento = authorNode.has("birth_year") ? authorNode.get("birth_year").asText() : "Desconocido";
                String fechaDeFallecimiento = authorNode.has("death_year") ? authorNode.get("death_year").asText() : "Desconocido";

                autores.add(new DatosAutor(nombre, fechaDeNacimiento, fechaDeFallecimiento));
            }
        }

        return new DatosLibros(titulo, autores, idioma, descargas);
    }

    public long obtenerCantidadDeLibrosPorIdioma(String idioma) {
        // Usando el método countByIdioma del repositorio
        return libroRepository.countByIdioma(idioma);
    }

}
