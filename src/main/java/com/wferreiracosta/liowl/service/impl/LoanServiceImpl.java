package com.wferreiracosta.liowl.service.impl;

import java.util.Optional;

import com.wferreiracosta.liowl.exception.BusinessException;
import com.wferreiracosta.liowl.model.entity.Loan;
import com.wferreiracosta.liowl.model.repository.LoanRepository;
import com.wferreiracosta.liowl.service.LoanService;

import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {

    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if (this.repository.existsByBookAndNotReturned(loan.getBook())) {
            throw new BusinessException("Book already loaned");
        }
        return this.repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return this.repository.save(loan);
    }

}