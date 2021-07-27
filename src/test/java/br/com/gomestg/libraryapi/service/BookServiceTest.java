package br.com.gomestg.libraryapi.service;

import br.com.gomestg.libraryapi.model.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @Test
    @DisplayName("save book")
    public void saveBookTest() {
        Book book = Book.builder()
                .title("Cassino Royale")
                .author("Ian Fleming")
                .isbn("U1234")
                .build();

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("U1234");
        assertThat(savedBook.getAuthor()).isEqualTo("Ian Fleming");
        assertThat(savedBook.getTitle()).isEqualTo("Cassino Royale");
    }
}
