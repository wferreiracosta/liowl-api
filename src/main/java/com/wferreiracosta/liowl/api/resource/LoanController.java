package com.wferreiracosta.liowl.api.resource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.wferreiracosta.liowl.api.dto.BookDTO;
import com.wferreiracosta.liowl.api.dto.LoanDTO;
import com.wferreiracosta.liowl.api.dto.LoanFilterDTO;
import com.wferreiracosta.liowl.api.dto.ReturnedLoanDTO;
import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.model.entity.Loan;
import com.wferreiracosta.liowl.service.BookService;
import com.wferreiracosta.liowl.service.LoanService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final ModelMapper modelMapper;

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

    @PatchMapping("{id}")
    public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto){
        Loan loan = this.service.getById(id)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));                
        loan.setReturned(dto.getReturned());
        this.service.update(loan);
    }

    @GetMapping
    public Page<LoanDTO> find(LoanFilterDTO loanFilterDTO, Pageable pageable){
        Page<Loan> result = this.service.find(loanFilterDTO, pageable);
        List<LoanDTO> loans = result
            .getContent()
            .stream()
            .map(entity -> {
                Book book = entity.getBook();
                BookDTO bookDTO = this.modelMapper.map(book, BookDTO.class);
                LoanDTO loanDTO = this.modelMapper.map(entity, LoanDTO.class);
                loanDTO.setBook(bookDTO);
                return loanDTO;
            }).collect(Collectors.toList());
        return new PageImpl<LoanDTO>(loans, pageable, result.getTotalElements());
    }
}
