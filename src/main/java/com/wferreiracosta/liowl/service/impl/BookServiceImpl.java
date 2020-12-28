package com.wferreiracosta.liowl.service.impl;

import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.model.repository.BookRepository;
import com.wferreiracosta.liowl.service.BookService;

import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return this.repository.save(book);
    }

}