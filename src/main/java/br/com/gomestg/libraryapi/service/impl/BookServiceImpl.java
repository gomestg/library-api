package br.com.gomestg.libraryapi.service.impl;

import br.com.gomestg.libraryapi.exception.LibraryBusinessException;
import br.com.gomestg.libraryapi.model.entity.Book;
import br.com.gomestg.libraryapi.model.repository.BookRepository;
import br.com.gomestg.libraryapi.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())){
            throw new LibraryBusinessException("Isbn already registered");
        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(Book book) {
        if (book.getId() == null || book == null){
            throw new IllegalArgumentException("Book ID can't be null");
        }
        this.repository.delete(book);
    }

    @Override
    public Book update(Book book) {
        if (book.getId() == null || book == null){
            throw new IllegalArgumentException("Book ID can't be null");
        }
        return this.repository.save(book);
    }
}
