package com.wferreiracosta.liowl.api.resource;

import java.time.LocalDate;

import com.wferreiracosta.liowl.api.dto.LoanDTO;
import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.model.entity.Loan;
import com.wferreiracosta.liowl.service.BookService;
import com.wferreiracosta.liowl.service.LoanService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO loanDTO){
        Book book = bookService
            .getBookByIsbn(loanDTO.getIsbn())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));

        Loan entity = Loan.builder()
            .book(book)
            .customer(loanDTO.getCustomer())
            .loanDate(LocalDate.now())
            .build();

        entity = service.save(entity);
        return entity.getId();
    }
}
