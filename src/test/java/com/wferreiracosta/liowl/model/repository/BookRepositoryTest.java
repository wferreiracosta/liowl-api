package com.wferreiracosta.liowl.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.wferreiracosta.liowl.model.entity.Book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    TestEntityManager entityManager;
    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o ISBN informado")
    public void returnTrueWhenIsbnExists() {
        String isbn = "123";
        Book book = createNewBook(isbn);
        this.entityManager.persist(book);
        boolean existsByIsbn = this.repository.existsByIsbn(isbn);
        assertThat(existsByIsbn).isTrue();
    }

    private Book createNewBook(String isbn) {
        return Book.builder()
            .author("Fulano")
            .isbn(isbn)
            .title("Aventuras")
            .build();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com o ISBN informado")
    public void returnFalseWhenIsbnDoesntExist() {
        String isbn = "123";
        boolean existsByIsbn = this.repository.existsByIsbn(isbn);
        assertThat(existsByIsbn).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void findByIdTest(){
        Book book = this.createNewBook("123");
        this.entityManager.persist(book);

        Optional<Book> foundBook = this.repository.findById(book.getId());

        assertThat(foundBook.isPresent()).isTrue();
    }
}
