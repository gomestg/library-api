package br.com.gomestg.libraryapi.model.repository;

import br.com.gomestg.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);

}
