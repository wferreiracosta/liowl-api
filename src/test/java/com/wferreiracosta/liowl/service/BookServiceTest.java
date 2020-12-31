package com.wferreiracosta.liowl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import com.wferreiracosta.liowl.exception.BusinessException;
import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.model.repository.BookRepository;
import com.wferreiracosta.liowl.service.impl.BookServiceImpl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
    BookService service;
    @MockBean
    BookRepository repository;

    private Book createValidBook() {
        return Book.builder()
            .isbn("123")
            .author("Fulano")
            .title("As aventuras")
            .build();
    }

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }
    
    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBook(){
        Book book = createValidBook();
        Mockito.when(this.repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(this.repository.save(book))
            .thenReturn(Book.builder()
                .id(11L)
                .isbn("123")
                .author("Fulano")
                .title("As aventuras")
                .build()
        );

        Book savedBook = service.save(book);

        assertNotNull(savedBook.getId());
        assertEquals(book.getIsbn(), savedBook.getIsbn());
        assertEquals(book.getTitle(), savedBook.getTitle());
        assertEquals(book.getAuthor(), savedBook.getAuthor());
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com ISBN duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        Book book = this.createValidBook();
        Mockito.when(this.repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        Throwable exception = Assertions.catchThrowable( () -> this.service.save(book) );
        
        String errorMessage = "ISBN já cadastrada";

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage(errorMessage);

        Mockito.verify(this.repository, Mockito.never()).save(book);
    }
}