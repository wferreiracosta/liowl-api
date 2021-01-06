package com.wferreiracosta.liowl.api.resource;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.wferreiracosta.liowl.api.dto.BookDTO;
import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.service.BookService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public BookDTO create( @RequestBody @Valid BookDTO bookDTO ){
        Book entity = this.modelMapper.map(bookDTO, Book.class);
        Book savedBook = this.service.save(entity);
        BookDTO savedBookDTO = this.modelMapper.map(savedBook, BookDTO.class);
        return savedBookDTO;
    }

    @GetMapping("{id}")
    public BookDTO get(@PathVariable Long id){
        return this.service
            .getById(id)
            .map( book -> modelMapper.map(book, BookDTO.class) )
            .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        Book book = this.service
            .getById(id)
            .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        this.service.delete(book);
    }

    @PutMapping("{id}")
    public BookDTO update(@PathVariable Long id, BookDTO bookDTO){
        return this.service
            .getById(id)
            .map( book -> {
                book.setAuthor(bookDTO.getAuthor());
                book.setTitle(bookDTO.getTitle());
                Book updateBook = this.service.update(book);
                return modelMapper.map(updateBook, BookDTO.class);
            })
            .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Page<BookDTO> find(BookDTO bookDTO, Pageable pageRequest){
        Book filter = this.modelMapper.map(bookDTO, Book.class);
        Page<Book> result = this.service.find(filter, pageRequest);
        List<BookDTO> list = result.getContent()
            .stream()
            .map(entity -> modelMapper.map(entity, BookDTO.class))
            .collect(Collectors.toList());
        return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements());
    }

}