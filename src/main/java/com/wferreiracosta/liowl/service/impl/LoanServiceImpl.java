package com.wferreiracosta.liowl.service.impl;

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
    public Loan save(Loan any) {
        return this.repository.save(any);
    }

}