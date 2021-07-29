package br.com.gomestg.libraryapi.model.repository;

import br.com.gomestg.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("is true when isbn exists")
    public void isTrueIsbnExistsTest() {
        Book book = createNewBook();
        entityManager.persist(book);

        boolean exist = repository.existsByIsbn(book.getIsbn());

        assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("is false when isbn doesn't exists")
    public void isFalseIsbnDoestExistsTest() {
        Book book = createNewBook();
        boolean exist = repository.existsByIsbn(book.getIsbn());

        assertThat(exist).isFalse();
    }

    @Test
    @DisplayName("find book by id")
    public void findByIdTest(){
        Book book = createNewBook();
        entityManager.persist(book);

        Optional<Book> foundBook = repository.findById(book.getId());

        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("save a book")
    public void saveBookTest(){
        Book book = createNewBook();

        Book savedBook = repository.save(book);

        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("delete a book")
    public void deleteBookTest(){
        Book book = createNewBook();
        entityManager.persist(book);

        Book foundBook =  entityManager.find(Book.class, book.getId());
        repository.delete(foundBook);

        Book deletedBook = entityManager.find(Book.class, book.getId());
        assertThat(deletedBook).isNotNull();
    }

    private Book createNewBook() {
        return Book.builder()
                .title("Cassino Royale")
                .author("Ian Fleming")
                .isbn("U1234")
                .build();
    }
}
