package com.alura.literalura.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GutendexClient {

    private final HttpClient client;

    // Constructor para inicializar el HttpClient
    public GutendexClient() {
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Método para obtener libros de la API Gutendex.
     *
     * @return Respuesta JSON con los datos de los libros.
     */
    public String fetchBooks() {
        try {
            // Construir la solicitud HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://gutendex.com/books/"))
                    .GET()
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Devolver el cuerpo de la respuesta como String
            return response.body();
        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace();
            return null;
        }
    }
}
