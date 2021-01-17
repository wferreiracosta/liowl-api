package com.wferreiracosta.liowl.service.impl;

import java.util.List;

import com.wferreiracosta.liowl.service.EmailService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${application.mail.default.remetent}")
    private String remetent;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMails(String message, List<String> mailsList) {
        String[] mails = mailsList.toArray(new String[mailsList.size()]);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(this.remetent);
        mailMessage.setSubject("Livro com emprestimo atrasado!");
        mailMessage.setText(message);
        mailMessage.setTo(mails);

        this.javaMailSender.send(mailMessage);
    }

}
