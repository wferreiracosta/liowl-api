package com.wferreiracosta.liowl.model.repository;

import com.wferreiracosta.liowl.model.entity.Book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    
}
