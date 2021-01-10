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

    public static Book createNewBook(String isbn) {
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
        Book book = createNewBook("123");
        this.entityManager.persist(book);

        Optional<Book> foundBook = this.repository.findById(book.getId());

        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        Book book = createNewBook("123");

        Book savedBook = this.repository.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getId()).isEqualTo(book.getId());
        assertThat(savedBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(savedBook.getIsbn()).isEqualTo(book.getIsbn());
        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    @DisplayName("Deve apagar um livro")
    public void deleteBookTest(){
        Book book = createNewBook("123");
        this.entityManager.persist(book);
        Book foundBook = this.entityManager.find(Book.class, book.getId());
        this.repository.delete(foundBook);
        Book deletedBook = this.entityManager.find(Book.class, book.getId());
        assertThat(deletedBook).isNull();
    }
}
