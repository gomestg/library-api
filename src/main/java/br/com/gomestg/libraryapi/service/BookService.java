package br.com.gomestg.libraryapi.service;

import br.com.gomestg.libraryapi.model.entity.Book;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    Book save(Book book);
}
