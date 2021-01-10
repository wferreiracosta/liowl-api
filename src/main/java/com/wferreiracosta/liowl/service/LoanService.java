package com.wferreiracosta.liowl.service;

import java.util.Optional;

import com.wferreiracosta.liowl.model.entity.Loan;

public interface LoanService {

	Loan save(Loan loan);

	Optional<Loan> getById(Long id);

	void update(Loan loan);

}