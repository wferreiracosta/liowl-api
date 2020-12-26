package com.wferreiracosta.liowl.api.resource;

import com.wferreiracosta.liowl.api.dto.BookDTO;
import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.service.BookService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {
    
    private BookService service;

    public BookController(BookService service) {
        this.service = service;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create( @RequestBody BookDTO bookDTO ){
        Book entity = Book.builder()
            .author(bookDTO.getAuthor())
            .isbn(bookDTO.getIsbn())
            .title(bookDTO.getTitle())
            .build();

        Book savedBook = this.service.save(entity);

        BookDTO savedBookDTO = BookDTO.builder()
            .id(savedBook.getId())
            .author(savedBook.getAuthor())
            .isbn(savedBook.getIsbn())
            .title(savedBook.getTitle())
            .build();
        
        return savedBookDTO;
    }

}
