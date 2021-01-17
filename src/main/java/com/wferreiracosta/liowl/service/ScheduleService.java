package com.wferreiracosta.liowl.service;

import java.util.List;
import java.util.stream.Collectors;

import com.wferreiracosta.liowl.model.entity.Loan;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";
    private static final String TIME_ZONE = "America/Sao_Paulo";

    private final LoanService loanService;
    private final EmailService emailService;

    @Value("${application.mail.lateloans.message}")
    private String message;

    @Scheduled(cron = CRON_LATE_LOANS, zone = TIME_ZONE)
    public void sendMailToLateLoans(){
        List<Loan> allLateLoans = this.loanService.getAllLateLoans();
        List<String> mailsList = allLateLoans.stream()
            .map(loan -> loan.getCustomerEmail())
            .collect(Collectors.toList());

        this.emailService.sendMails(this.message, mailsList);
    }

}
