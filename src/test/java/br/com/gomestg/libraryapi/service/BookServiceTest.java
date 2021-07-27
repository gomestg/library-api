package br.com.gomestg.libraryapi.service;

import br.com.gomestg.libraryapi.exception.LibraryBusinessException;
import br.com.gomestg.libraryapi.model.entity.Book;
import br.com.gomestg.libraryapi.model.repository.BookRepository;
import br.com.gomestg.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("save book")
    public void saveBookTest() {
        Book book = createValidBook();

        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book))
                .thenReturn(Book.builder()
                        .id(10l)
                        .isbn("U1234")
                        .title("Cassino Royale")
                        .author("Ian Fleming")
                        .build());

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("U1234");
        assertThat(savedBook.getAuthor()).isEqualTo("Ian Fleming");
        assertThat(savedBook.getTitle()).isEqualTo("Cassino Royale");
    }

    @Test
    @DisplayName("error business: create book with isbn duplicated")
    public void shouldNotSaveABookWithDuplicatedIsbn() {
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        assertThat(exception)
                .isInstanceOf(LibraryBusinessException.class)
                .hasMessage("Isbn already registered");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    private Book createValidBook() {
        return Book.builder()
                .title("Cassino Royale")
                .author("Ian Fleming")
                .isbn("U1234")
                .build();
    }
}
