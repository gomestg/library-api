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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void shouldNotSaveABookWithDuplicatedIsbnTest() {
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        assertThat(exception)
                .isInstanceOf(LibraryBusinessException.class)
                .hasMessage("Isbn already registered");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("find book by id")
    public void getByIdTest() {
        Long id = 10l;

        Book book = createValidBook();
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.getById(id);
        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("not found book by id ")
    public void bookNotFoundByIdTest() {
        Long id = 10l;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isFalse();
    }

    @Test
    @DisplayName("delete a book")
    public void deleteBookTest() {
        Book book = Book.builder().id(10l).build();

        assertDoesNotThrow(() -> service.delete(book));

        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("delete invalid book")
    public void deleteInvalidBookTest() {
        Book book = new Book();

        assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("update a book")
    public void updateBookTest() {
        Long id = 10l;
        Book updatingBook = Book.builder().id(id).build();

        Book updatedBook = createValidBook();
        updatedBook.setId(id);
        Mockito.when(repository.save(updatingBook)).thenReturn(updatedBook);

        Book book = service.update(updatingBook);

        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
    }

    @Test
    @DisplayName("update invalid book")
    public void updateInvalidBookTest() {
        Book book = new Book();

        assertThrows(IllegalArgumentException.class, () -> service.update(book));

        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("find book by properties")
    public void findBookByPropertiesTest() {
        Book book = createValidBook();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<Book>(Arrays.asList(book), pageRequest, 1);
        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

        Page<Book> result = service.find(book, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(Arrays.asList(book));
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    private Book createValidBook() {
        return Book.builder()
                .title("Cassino Royale")
                .author("Ian Fleming")
                .isbn("U1234")
                .build();
    }
}
