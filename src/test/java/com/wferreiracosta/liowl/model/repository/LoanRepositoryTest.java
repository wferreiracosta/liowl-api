package com.wferreiracosta.liowl.model.repository;

import static com.wferreiracosta.liowl.model.repository.BookRepositoryTest.createNewBook;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.model.entity.Loan;
import com.wferreiracosta.liowl.service.EmailService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    LoanRepository repository;
    @Autowired
    TestEntityManager entityManager;
    @MockBean
    EmailService emailService;

    @Test
    @DisplayName("Deve verificar se existe emprestimo não devolvido para o livro")
    public void existsByBookAndNotReturnedTest(){
        Loan loan = createAndPersistLoan(LocalDate.now());
        Book book = loan.getBook();

        boolean exists = this.repository.existsByBookAndNotReturned(book);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve buscar empréstimo pelo isbn do livro ou customer")
    public void findByBookIsbnOrCustomerTest(){
        Loan loan = createAndPersistLoan(LocalDate.now());

        Page<Loan> result = this.repository.findByBookIsbnOrCustomer(loan.getBook().getIsbn(), loan.getCustomer(), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(loan);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    private Loan createAndPersistLoan(LocalDate loanDate) {
        Book book = createNewBook("123");
        this.entityManager.persist(book);

        Loan loan = Loan.builder()
            .book(book)
            .customer("Fulano")
            .loanDate(loanDate)
            .build();
        this.entityManager.persist(loan);
        return loan;
    }

    @Test
    @DisplayName("Deve obter empréstimo cuja a data de emprestimo for menos ou igual a três dias atrâs e não retornadas")
    public void findByLoanDateLessThanAndNotReturnedTest(){
        //Cenário
        Loan loan = this.createAndPersistLoan(LocalDate.now().minusDays(5));
        //Execução
        List<Loan> result = this.repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));
        //Teste
        assertThat(result).hasSize(1).contains(loan);
    }

    @Test
    @DisplayName("Deve retornar vazio quando não houver emprestimos atrasados")
    public void notFindByLoanDateLessThanAndNotReturnedTest(){
        this.createAndPersistLoan(LocalDate.now());
        List<Loan> result = this.repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));
        assertThat(result).isEmpty();
    }
}
