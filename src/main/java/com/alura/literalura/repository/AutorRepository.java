package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Método para encontrar autores vivos en un año específico
    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :year " +
            "AND (a.fechaDeFallecimiento IS NULL OR a.fechaDeFallecimiento >= :year)")
    List<Autor> findAutoresVivosEnAnio(Integer year);
}
