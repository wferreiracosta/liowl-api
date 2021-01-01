package com.wferreiracosta.liowl.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

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
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBook() {
        Book book = createValidBook();
        Mockito.when(this.repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(this.repository.save(book))
                .thenReturn(Book.builder().id(11L).isbn("123").author("Fulano").title("As aventuras").build());

        Book savedBook = service.save(book);

        assertNotNull(savedBook.getId());
        assertEquals(book.getIsbn(), savedBook.getIsbn());
        assertEquals(book.getTitle(), savedBook.getTitle());
        assertEquals(book.getAuthor(), savedBook.getAuthor());
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com ISBN duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN() {
        Book book = this.createValidBook();
        Mockito.when(this.repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        Throwable exception = Assertions.catchThrowable(() -> this.service.save(book));

        String errorMessage = "ISBN já cadastrada";

        assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(errorMessage);

        Mockito.verify(this.repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void getByIdTest() {
        Long id = 1L;
        Book book = this.createValidBook();
        book.setId(id);
        Mockito.when(this.repository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = this.service.getById(id);

        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(book.getId());
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por id quando ele não existe na base.")
    public void bookNotFoundByIdTest() {
        Long id = 1L;
        Mockito.when(this.repository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> book = this.service.getById(id);

        assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() {
        Book book = Book.builder().id(11L).build();
        assertDoesNotThrow(() -> this.service.delete(book));
        Mockito.verify(this.repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar apagar um livro inexistente")
    public void deleteInvalidBookTest(){
        Book book = new Book();
        assertThrows(IllegalArgumentException.class, () -> this.service.delete(book));
        Mockito.verify(this.repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar atualizar um livro inexistente")
    public void updateinvalidBookTest(){
        Book book = new Book();
        assertThrows(IllegalArgumentException.class, () -> this.service.update(book));
        Mockito.verify(this.repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest(){
        //Cenário
        Long id = 11L;

        //Livro a atualizar
        Book updatingBook = Book.builder().id(id).build();
        
        //Simulação
        Book updatedBook = this.createValidBook();
        updatedBook.setId(id);
        Mockito.when(this.repository.save(updatingBook)).thenReturn(updatedBook);

        //Execução
        Book book = this.service.update(updatingBook);

        //Verificações
        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
    }

}