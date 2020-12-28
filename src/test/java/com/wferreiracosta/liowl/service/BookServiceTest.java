package com.wferreiracosta.liowl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.service.impl.BookServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
    BookService service;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBook(){
        Book book = Book.builder()
                        .isbn("123")
                        .author("Fulano")
                        .title("As aventuras")
                        .build();
        
        Book savedBook = service.save(book);

        assertNotNull(savedBook.getId());
        assertEquals(book.getIsbn(), savedBook.getIsbn());
        assertEquals(book.getTitle(), savedBook.getTitle());
        assertEquals(book.getAuthor(), savedBook.getAuthor());
    }
}