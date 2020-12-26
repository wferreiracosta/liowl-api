package com.wferreiracosta.liowl.api.resource;

import com.wferreiracosta.liowl.api.dto.BookDTO;
import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.service.BookService;

import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create( @RequestBody BookDTO bookDTO ){
        Book entity = this.modelMapper.map(bookDTO, Book.class);
        Book savedBook = this.service.save(entity);
        BookDTO savedBookDTO = this.modelMapper.map(savedBook, BookDTO.class);
        return savedBookDTO;
    }

}
