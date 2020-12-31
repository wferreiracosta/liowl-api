package com.wferreiracosta.liowl.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
        Book book = Book.builder()
            .author("Fulano")
            .isbn(isbn)
            .title("Aventuras")
            .build();
        this.entityManager.persist(book);
        boolean existsByIsbn = this.repository.existsByIsbn(isbn);
        assertThat(existsByIsbn).isTrue();
    }
}
