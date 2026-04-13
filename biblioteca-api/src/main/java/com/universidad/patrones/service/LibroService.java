package com.universidad.patrones.service;

import com.universidad.patrones.model.Libro;
import com.universidad.patrones.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Capa de servicio para la gestión de libros.
 *
 * Responsabilidad: contiene TODA la lógica de negocio:
 *   - Validaciones de unicidad (ISBN no duplicado)
 *   - Manejo de excepciones de dominio
 *   - Orquestación de operaciones sobre el repositorio
 *
 * IMPORTANTE: El Controller NUNCA debe acceder al Repository directamente.
 * Esta separación permite cambiar la lógica de negocio sin tocar la capa HTTP.
 *
 * @Transactional garantiza que cada operación se ejecute dentro de una
 * transacción de base de datos; si ocurre un error, se hace rollback automático.
 */
@Service
@Transactional
public class LibroService {

    // Inyección por constructor (buena práctica: facilita pruebas unitarias)
    private final LibroRepository repo;

    public LibroService(LibroRepository repo) {
        this.repo = repo;
    }

    /**
     * Retorna todos los libros registrados.
     */
    public List<Libro> findAll() {
        return repo.findAll();
    }

    /**
     * Busca un libro por su ID.
     * Retorna Optional vacío si no existe (el Controller decide el código HTTP).
     */
    public Optional<Libro> findById(Long id) {
        return repo.findById(id);
    }

    /**
     * Guarda un nuevo libro.
     * Valida que el ISBN no esté duplicado antes de persistir.
     *
     * @throws IllegalArgumentException si ya existe un libro con el mismo ISBN
     */
    public Libro save(Libro libro) {
        if (repo.existsByIsbn(libro.getIsbn())) {
            throw new IllegalArgumentException(
                "Ya existe un libro con ISBN: " + libro.getIsbn()
            );
        }
        return repo.save(libro);
    }

    /**
     * Actualiza los datos de un libro existente.
     * El ISBN no se actualiza para mantener integridad referencial.
     *
     * @throws NoSuchElementException si el libro con el ID dado no existe
     */
    public Libro update(Long id, Libro datos) {
        Libro existente = repo.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Libro no encontrado con id: " + id
            ));

        existente.setTitulo(datos.getTitulo());
        existente.setAutor(datos.getAutor());
        existente.setCategoria(datos.getCategoria());
        existente.setAnioPublicacion(datos.getAnioPublicacion());

        return repo.save(existente);
    }

    /**
     * Elimina un libro por su ID.
     *
     * @throws NoSuchElementException si el libro no existe
     */
    public void deleteById(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException(
                "Libro no encontrado con id: " + id
            );
        }
        repo.deleteById(id);
    }

    /**
     * Busca libros cuyo título contenga la palabra indicada.
     */
    public List<Libro> buscarPorPalabra(String palabra) {
        return repo.buscarPorPalabra(palabra);
    }
}
