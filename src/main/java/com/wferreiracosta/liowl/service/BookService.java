package com.wferreiracosta.liowl.service;

import java.util.Optional;

import com.wferreiracosta.liowl.model.entity.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

	public Book save(Book book);

	public Optional<Book> getById(long id);

	public void delete(Book book);

	public Book update(Book book);

	public Page<Book> find(Book filter, Pageable pageRequest);

}