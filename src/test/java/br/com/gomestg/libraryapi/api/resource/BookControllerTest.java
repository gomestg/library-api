package br.com.gomestg.libraryapi.api.resource;

import br.com.gomestg.libraryapi.api.dto.BookDTO;
import br.com.gomestg.libraryapi.exception.LibraryBusinessException;
import br.com.gomestg.libraryapi.model.entity.Book;
import br.com.gomestg.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static String URL = "/api/books";

    @Autowired
    MockMvc mock;

    @MockBean
    BookService service;

    @Test
    @DisplayName("successfully: created book")
    public void createBookTest() throws Exception {
        BookDTO dto = createNewBook();

        Book savedBook = Book.builder()
                .id(10l)
                .title("Cassino Royale")
                .author("Ian Fleming")
                .isbn("U1234")
                .build();

        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mock.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(10l))
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()))
        ;
    }

    @Test
    @DisplayName("error validation: create book with incomplete information")
    public void createInvalidBookTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mock.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));
    }

    @Test
    @DisplayName("error validation: create book with isbn duplicated")
    public void createBookDuplicatedIsbn() throws Exception {
        String msg = "Isbn already registered";
        String json = new ObjectMapper().writeValueAsString(createNewBook());

        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willThrow(new LibraryBusinessException(msg));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mock.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(msg));
    }

    private BookDTO createNewBook() {
        return BookDTO.builder()
                .title("Cassino Royale")
                .author("Ian Fleming")
                .isbn("U1234")
                .build();
    }
}
