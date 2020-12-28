package com.wferreiracosta.liowl.service;

import com.wferreiracosta.liowl.model.entity.Book;

import org.springframework.stereotype.Service;

@Service
public interface BookService {

	public Book save(Book book);
}