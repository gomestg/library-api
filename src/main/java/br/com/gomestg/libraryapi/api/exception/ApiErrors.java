package br.com.gomestg.libraryapi.api.exception;

import br.com.gomestg.libraryapi.exception.LibraryBusinessException;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {
    private List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public ApiErrors(LibraryBusinessException e) {
        this.errors = Arrays.asList(e.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
