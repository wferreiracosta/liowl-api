package com.wferreiracosta.liowl.model.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static com.wferreiracosta.liowl.model.repository.BookRepositoryTest.createNewBook;

import java.time.LocalDate;

import com.wferreiracosta.liowl.model.entity.Book;
import com.wferreiracosta.liowl.model.entity.Loan;

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
public class LoanRepositoryTest {

    @Autowired
    LoanRepository repository;
    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("Deve verificar se existe emprestimo não devolvido para o livro")
    public void existsByBookAndNotReturnedTest(){
        Book book = createNewBook("123");
        this.entityManager.persist(book);

        Loan loan = Loan.builder()
            .book(book)
            .customer("Fulano")
            .loanDate(LocalDate.now())
            .build();
        this.entityManager.persist(loan);

        boolean exists = this.repository.existsByBookAndNotReturned(book);

        assertThat(exists).isTrue();
    }

}
