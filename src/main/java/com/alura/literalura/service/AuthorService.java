package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import com.alura.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> obtenerAutoresVivosEnAnio(Integer year) {
        // Llamamos al repositorio para obtener los autores vivos en el año dado
        return autorRepository.findAutoresVivosEnAnio(year);
    }
}
