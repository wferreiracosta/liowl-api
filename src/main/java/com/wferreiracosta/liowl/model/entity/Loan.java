package com.wferreiracosta.liowl.model.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    private Long id;
    private String isbn;
    private String customer;
    private Book book;
    private LocalDate loanDate;
    private Boolean returned;

}
