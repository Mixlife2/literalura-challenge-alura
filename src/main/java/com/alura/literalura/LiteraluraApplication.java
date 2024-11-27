package com.alura.literalura;

import com.alura.literalura.model.DatosAutor;
import com.alura.literalura.model.DatosLibros;
import com.alura.literalura.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	private final BookService bookService;
	private final List<DatosLibros> librosBuscados = new ArrayList<>();

	public LiteraluraApplication(BookService bookService) {
		this.bookService = bookService;
	}

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) {
		mostrarMenu();
	}

	private void mostrarMenu() {
		Scanner scanner = new Scanner(System.in);
		int opcion;

		do {
			System.out.println("\n--- Menú Principal ---");
			System.out.println("1. Buscar libro por título");
			System.out.println("2. Listar todos los libros buscados");
			System.out.println("3. Listar libros por idioma");
			System.out.println("5. Listar todos los autores");
			System.out.println("6. Listar autores vivos en un año");
			System.out.println("4. Salir");

			System.out.print("Seleccione una opción: ");

			while (!scanner.hasNextInt()) {
				System.out.println("Por favor, ingrese un número válido.");
				scanner.next();
			}
			opcion = scanner.nextInt();
			scanner.nextLine(); // Limpiar el buffer

			switch (opcion) {
				case 1 -> buscarLibro(scanner);
				case 2 -> listarTodosLosLibros();
				case 3 -> listarLibrosPorIdioma(scanner);
				case 5 -> listarTodosLosAutores();  // Agregar esta opción
				case 6 -> listarAutoresVivosEnAnio(scanner);

				case 4 -> System.out.println("Saliendo del programa. ¡Hasta pronto!");
				default -> System.out.println("Opción no válida. Intente nuevamente.");
			}
		} while (opcion != 4);

		scanner.close();
	}

	private void buscarLibro(Scanner scanner) {
		System.out.print("Ingrese el título del libro que desea buscar: ");
		String titulo = scanner.nextLine();
		try {
			// Llamada al servicio para buscar el libro por título
			DatosLibros libro = bookService.buscarLibroPorTitulo(titulo);

			// Agregar solo el primer autor a la lista de autores
			if (!libro.autor().isEmpty()) {
				DatosAutor primerAutor = libro.autor().get(0);
				if (!autoresBuscados.contains(primerAutor)) {
					autoresBuscados.add(primerAutor);
				}
			}


			System.out.println("\nLibro encontrado:");
			imprimirLibro(libro);  // Mostrar detalles del libro (puedes agregarlo según lo necesites)
		} catch (Exception e) {
			System.out.println("Error al buscar el libro: " + e.getMessage());
		}
	}


	private void listarTodosLosLibros() {
		if (librosBuscados.isEmpty()) {
			System.out.println("No se han buscado libros todavía.");
			return;
		}
		System.out.println("\n--- Lista de Libros Buscados ---");
		librosBuscados.forEach(this::imprimirLibro);
	}

	private void listarLibrosPorIdioma(Scanner scanner) {
		System.out.print("Ingrese el idioma (código ISO) para filtrar libros: ");
		String idioma = scanner.nextLine();

		System.out.println("\n--- Libros en el idioma " + idioma + " ---");
		librosBuscados.stream()
				.filter(libro -> libro.idioma().equalsIgnoreCase(idioma))
				.forEach(this::imprimirLibro);
	}

	private void imprimirLibro(DatosLibros libro) {
		System.out.println("Título: " + libro.titulo());
		System.out.println("Idioma: " + libro.idioma());
		System.out.println("Descargas: " + libro.numeroDeDescargas());
		System.out.println("Autores:");
		libro.autor().forEach(autor -> System.out.println("  - " + autor.nombre()));
		System.out.println();
	}

	private void listarTodosLosAutores() {
		if (autoresBuscados.isEmpty()) {
			System.out.println("No se han buscado autores todavía.");
			return;
		}
		System.out.println("\n--- Lista de Autores ---");
		autoresBuscados.forEach(autor -> System.out.println("  - " + autor.nombre()));
	}

	private void listarAutoresVivosEnAnio(Scanner scanner) {
		System.out.print("Ingrese el año para filtrar autores vivos: ");
		int anioConsulta = scanner.nextInt();
		scanner.nextLine();  // Consumir la nueva línea

		System.out.println("\n--- Autores vivos en el año " + anioConsulta + " ---");
		autoresBuscados.stream()
				.filter(autor -> esAutorVivoEnAnio(autor, anioConsulta))
				.forEach(autor -> System.out.println("  - " + autor.nombre()));
	}

	private boolean esAutorVivoEnAnio(DatosAutor autor, int anioConsulta) {
		int anioNacimiento = Integer.parseInt(autor.fechaDeNacimiento());
		String fechaDeFallecimiento = autor.fechaDeFallecimiento();

		// Si el año de fallecimiento es "Desconocido", el autor está vivo
		if (fechaDeFallecimiento.equals("Desconocido")) {
			return anioNacimiento <= anioConsulta;
		}

		int anioFallecimiento = Integer.parseInt(fechaDeFallecimiento);
		return anioNacimiento <= anioConsulta && anioFallecimiento >= anioConsulta;
	}



	private final List<DatosAutor> autoresBuscados = new ArrayList<>();

}
