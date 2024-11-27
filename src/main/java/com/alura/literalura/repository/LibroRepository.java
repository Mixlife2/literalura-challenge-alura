package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Método que cuenta los libros por idioma
    long countByIdioma(String idioma);

    // Si prefieres una consulta más personalizada usando JPQL
    @Query("SELECT l.idioma, COUNT(l) FROM Libro l WHERE l.idioma = :idioma GROUP BY l.idioma")
    long countLibrosPorIdioma(String idioma);
}
