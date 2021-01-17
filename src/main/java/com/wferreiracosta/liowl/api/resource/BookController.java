package com.wferreiracosta.liowl.api.resource;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.wferreiracosta.liowl.api.dto.BookDTO;
import com.wferreiracosta.liowl.api.dto.LoanDTO;
import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.model.entity.Loan;
import com.wferreiracosta.liowl.service.BookService;
import com.wferreiracosta.liowl.service.LoanService;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Api("Book API")
@Slf4j
public class BookController {
    
    private final BookService service;
    private final LoanService loanService;
    private final ModelMapper modelMapper;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create a book")
    public BookDTO create( @RequestBody @Valid BookDTO bookDTO ){
        log.info("Creating a book for isbn: {}", bookDTO.getIsbn());
        Book entity = this.modelMapper.map(bookDTO, Book.class);
        Book savedBook = this.service.save(entity);
        BookDTO savedBookDTO = this.modelMapper.map(savedBook, BookDTO.class);
        return savedBookDTO;
    }

    @GetMapping("{id}") 
    @ApiOperation("Obtains a book details by id")
    public BookDTO get(@PathVariable Long id){
        log.info("Obtaining details for book id: {}", id);
        return this.service
            .getById(id)
            .map( book -> modelMapper.map(book, BookDTO.class) )
            .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deletes a book by id")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Book succesfully deleted")
    })
    public void delete(@PathVariable Long id){
        log.info("Delete book of id: {}", id);
        Book book = this.service
            .getById(id)
            .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        this.service.delete(book);
    }

    @PutMapping("{id}")
    @ApiOperation("Updates a book")
    public BookDTO update(@PathVariable Long id, BookDTO bookDTO){
        log.info("Update book of id: {}", id);
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
    @ApiOperation("Find books by params")
    public Page<BookDTO> find(BookDTO bookDTO, Pageable pageRequest){
        Book filter = this.modelMapper.map(bookDTO, Book.class);
        Page<Book> result = this.service.find(filter, pageRequest);
        List<BookDTO> list = result.getContent()
            .stream()
            .map(entity -> modelMapper.map(entity, BookDTO.class))
            .collect(Collectors.toList());
        return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements());
    }

    @GetMapping("{id}/loans")
    @ApiOperation("Gets book loans")
    public Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable){
        Book book = this.service.getById(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
            );
        Page<Loan> result = this.loanService.getLoansByBook(book, pageable);
        List<LoanDTO> list = result.getContent()
            .stream()
            .map(loan -> {
                Book loanBook = loan.getBook();
                BookDTO bookDTO = this.modelMapper.map(loanBook, BookDTO.class);
                LoanDTO loanDTO = this.modelMapper.map(loan, LoanDTO.class);
                loanDTO.setBook(bookDTO);
                return loanDTO;
            }).collect(Collectors.toList());
        return new PageImpl<LoanDTO>(list, pageable, result.getTotalElements());
    }

}