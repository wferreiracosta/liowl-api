package com.wferreiracosta.liowl.service;

import java.util.Optional;

import com.wferreiracosta.liowl.model.entity.Book;

public interface BookService {

	public Book save(Book book);

	public Optional<Book> getById(long id);

}