package br.com.gomestg.libraryapi.api.resource;

import br.com.gomestg.libraryapi.api.dto.BookDTO;
import br.com.gomestg.libraryapi.api.exception.ApiErrors;
import br.com.gomestg.libraryapi.exception.LibraryBusinessException;
import br.com.gomestg.libraryapi.model.entity.Book;
import br.com.gomestg.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);
        entity = service.save(entity);
        return modelMapper.map(entity, BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handlerValidationException(MethodArgumentNotValidException e){
        return new ApiErrors(e.getBindingResult());
    }

    @ExceptionHandler(LibraryBusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handlerValidationException(LibraryBusinessException e){
        return new ApiErrors(e);
    }

}
