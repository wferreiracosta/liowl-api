package com.wferreiracosta.liowl.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.wferreiracosta.liowl.api.dto.LoanFilterDTO;
import com.wferreiracosta.liowl.exception.BusinessException;
import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.model.entity.Loan;
import com.wferreiracosta.liowl.model.repository.LoanRepository;
import com.wferreiracosta.liowl.service.impl.LoanServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    LoanService service;
    @MockBean
    LoanRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new LoanServiceImpl(repository);
    }
    
    @Test
    @DisplayName("Deve salvar um emprestimo")
    public void saveLoanTest() {
        Book book = Book.builder().id(1l).build();
        String customer = "Fulano";

        Loan savingLoan =
            Loan.builder()
            .book(book)
            .customer(customer)
            .loanDate(LocalDate.now())
            .build();

        Loan savedLoan = Loan.builder()
            .id(1l)
            .loanDate(LocalDate.now())
            .customer(customer)
            .book(book).build();


        Mockito.when(repository.existsByBookAndNotReturned(book))
            .thenReturn(false);

        Mockito.when(repository.save(savingLoan))
            .thenReturn(savedLoan);

        Loan loan = service.save(savingLoan);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao salvar um emprestimo de livro ja emprestado")
    public void loanedBookSavedTest() {
        Book book = Book.builder().id(1l).build();
        String customer = "Fulano";

        Loan savingLoan =
            Loan.builder()
            .book(book)
            .customer(customer)
            .loanDate(LocalDate.now())
            .build();

        Mockito.when(repository.existsByBookAndNotReturned(book))
            .thenReturn(true);

        Throwable exception = catchThrowable( () -> service.save(savingLoan));

        assertThat(exception)
            .isInstanceOf(BusinessException.class)
            .hasMessage("Book already loaned");

        verify(repository, Mockito.never()).save(savingLoan);
    }

    @Test
    @DisplayName("Deve obter as informações de um empréstimo pelo ID")
    public void getLoanDetaisTest(){
        Long id = 1l;
        Loan loan = createLoan();
        loan.setId(id);

        Mockito
            .when(this.repository.findById(id))
            .thenReturn(Optional.of(loan));
        
        Optional<Loan> result = this.service.getById(id);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(loan.getId());
        assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
        assertThat(result.get().getBook().getId()).isEqualTo(loan.getBook().getId());
        assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());

        verify(repository, Mockito.times(1)).findById(id);
    }

    public static Loan createLoan(){
        Book book = Book.builder()
            .id(1l)
            .build();

        return Loan.builder()
            .book(book)
            .customer("Fulano")
            .loanDate(LocalDate.now())
            .build();
    }

    @Test
    @DisplayName("Deve atualizar um empréstimo")
    public void updateLoanTest(){
        Loan loan = createLoan();
        loan.setId(1L);
        loan.setReturned(true);

        Mockito.when(this.repository.save(loan))
            .thenReturn(loan);

        Loan updatedLoan = this.service.update(loan);

        assertThat(updatedLoan.getReturned()).isTrue();
        verify(this.repository).save(loan);
    }

    @Test
    @DisplayName("Deve filtrar emprestimos pelas propriedades")
    @SuppressWarnings("unchecked")
    public void findLoanTest(){
        //Cenario
        LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder()
            .customer("Fulano")
            .isbn("321")
            .build();

        Loan loan = createLoan();
        loan.setId(1L);

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Loan> list = Arrays.asList(loan);
        Page<Loan> page = new PageImpl<Loan>(list, pageRequest, list.size());
        Mockito.when(this.repository.findByBookIsbnOrCustomer(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(PageRequest.class)
            ))
            .thenReturn(page);

        //Execução
        Page<Loan> result = this.service.find(loanFilterDTO, pageRequest);

        //Verificações
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }
}
