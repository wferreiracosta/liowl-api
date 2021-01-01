package com.wferreiracosta.liowl.service.impl;

import java.util.Optional;

import com.wferreiracosta.liowl.exception.BusinessException;
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
        if (this.repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("ISBN já cadastrada");
        }
        return this.repository.save(book);
    }

    @Override
    public Optional<Book> getById(long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Book book) {
        
    }

    @Override
    public Book update(Book book) {
        return null;
    }

}