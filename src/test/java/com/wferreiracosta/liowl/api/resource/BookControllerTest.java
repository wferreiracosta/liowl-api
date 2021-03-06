package com.wferreiracosta.liowl.api.resource;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wferreiracosta.liowl.api.dto.BookDTO;
import com.wferreiracosta.liowl.exception.BusinessException;
import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.service.BookService;
import com.wferreiracosta.liowl.service.EmailService;
import com.wferreiracosta.liowl.service.LoanService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;
    
    @MockBean
    BookService service;

    @MockBean
    LoanService loanService;

    @MockBean
    EmailService emailService;
    
    private BookDTO createNewBook() {
        return BookDTO.builder()
            .author("Arthur")
            .isbn("001")
            .title("As Aventuras")
            .build();
    }

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTest() throws Exception {
        BookDTO dto = createNewBook();

        Book savedBook = Book.builder()
            .author("Arthur")
            .isbn("001")
            .title("As Aventuras")
            .id(10L)
            .build();

        BDDMockito
            .given(service.save(Mockito.any(Book.class)))
            .willReturn(savedBook);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(BOOK_API)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").isNotEmpty())
            .andExpect(jsonPath("title").value(dto.getTitle()))
            .andExpect(jsonPath("author").value(dto.getAuthor()))
            .andExpect(jsonPath("isbn").value(dto.getIsbn()));
    }


    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação de livro")
    public void createInvalidBookTest() throws Exception{
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(BOOK_API)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);
        
        mvc.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errors", hasSize(3)));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar um livro com ISBN já utilizado por outro")
    public void createBookWithDuplicateIsbn() throws Exception {
        BookDTO dto = this.createNewBook();
        String json = new ObjectMapper().writeValueAsString(dto);

        String errorMessage = "ISBN já cadastrada";

        BDDMockito
            .given(this.service.save(Mockito.any(Book.class)))
            .willThrow(new BusinessException(errorMessage));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errors", hasSize(1)))
            .andExpect(jsonPath("errors[0]").value(errorMessage));
    }

    @Test
    @DisplayName("Deve obter informações de um livro")
    public void getBookDetailsTest() throws Exception {
        long id = 1L;

        Book book = Book.builder()
            .id(id)
            .title(this.createNewBook().getTitle())
            .author(this.createNewBook().getAuthor())
            .isbn(this.createNewBook().getIsbn())
            .build();

        BDDMockito
            .given(this.service.getById(id))
            .willReturn(Optional.of(book));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(BOOK_API.concat("/"+id))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").isNotEmpty())
            .andExpect(jsonPath("id").value(book.getId()))
            .andExpect(jsonPath("title").value(book.getTitle()))
            .andExpect(jsonPath("author").value(book.getAuthor()))
            .andExpect(jsonPath("isbn").value(book.getIsbn()));
    }

    @Test
    @DisplayName("Deve retornar resource not found quando o livro procurado não existir")
    public void bookNotFoundTest() throws Exception {
        BDDMockito
            .given(this.service.getById(Mockito.anyLong()))
            .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(BOOK_API.concat("/"+1))
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() throws Exception {
        Long id = 11L;

        BDDMockito
            .given(this.service.getById(anyLong()))
            .willReturn(Optional.of(Book.builder().id(id).build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete(BOOK_API.concat("/"+id));
        
        mvc.perform(request)
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar resource not found quando não encontrar o livro para deletar")
    public void deleteInexistentBookTest() throws Exception {
        Long id = 11L;

        BDDMockito
            .given(this.service.getById(anyLong()))
            .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete(BOOK_API.concat("/"+id));
        
        mvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest() throws Exception {
        Long id = 11L;
        String json = new ObjectMapper().writeValueAsString(this.createNewBook());

        Book updatingBook = Book.builder()
            .id(id)
            .title("some title")
            .author("some author")
            .isbn("001")
            .build();

        Book updatedBook = Book.builder()
            .id(id)
            .author("Arthur")
            .isbn("001")
            .title("As Aventuras")
            .build();

        BDDMockito
            .given(this.service.getById(anyLong()))
            .willReturn(Optional.of(updatingBook));

        BDDMockito
            .given(this.service.update(updatingBook))
            .willReturn(updatedBook);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put(BOOK_API.concat("/"+id))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);
        
        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").isNotEmpty())
            .andExpect(jsonPath("id").value(updatedBook.getId()))
            .andExpect(jsonPath("title").value(updatedBook.getTitle()))
            .andExpect(jsonPath("author").value(updatedBook.getAuthor()))
            .andExpect(jsonPath("isbn").value(this.createNewBook().getIsbn()));
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar um livro inexistente")
    public void updateInexistentBookTest() throws Exception {
        Long id = 11L;
        String json = new ObjectMapper().writeValueAsString(this.createNewBook());

        BDDMockito
            .given(this.service.getById(anyLong()))
            .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put(BOOK_API.concat("/"+id))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);
        
        mvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve filtrar livros")
    public void findBookTest() throws Exception {
        Long id = 1l;

        Book book = Book.builder()
            .id(id)
            .title(createNewBook().getTitle())
            .author(createNewBook().getAuthor())
            .isbn(createNewBook().getIsbn())
            .build();

        BDDMockito.given(service.find(Mockito.any(Book.class), Mockito.any(Pageable.class)))
            .willReturn(new PageImpl<Book>( Arrays.asList(book), PageRequest.of(0,100),1));

        String queryString = String.format("?title=%s&author=%s&page=0&size=100",
            book.getTitle(),
            book.getAuthor()
        );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(BOOK_API.concat(queryString))
            .accept(MediaType.APPLICATION_JSON);

        mvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("content", Matchers.hasSize(1)))
            .andExpect(jsonPath("totalElements").value(1))
            .andExpect(jsonPath("pageable.pageSize").value(100))
            .andExpect(jsonPath("pageable.pageNumber").value(0));

    }

}