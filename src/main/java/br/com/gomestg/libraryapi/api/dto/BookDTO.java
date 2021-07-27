package br.com.gomestg.libraryapi.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String author;
    @NotEmpty
    private String isbn;
}
