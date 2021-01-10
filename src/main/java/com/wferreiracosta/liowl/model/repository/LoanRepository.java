package com.wferreiracosta.liowl.model.repository;

import com.wferreiracosta.liowl.model.entity.Loan;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    
}
