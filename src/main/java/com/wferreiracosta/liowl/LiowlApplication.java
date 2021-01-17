package com.wferreiracosta.liowl;

import java.util.Arrays;
import java.util.List;

import com.wferreiracosta.liowl.service.EmailService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LiowlApplication {

	@Autowired
	private EmailService emailService;

	@Bean
	public CommandLineRunner runner(){
		return args -> {
			List<String> emails = Arrays.asList("liowl-23ea63@inbox.mailtrap.io");
			this.emailService.sendMails("Testando serviço de email", emails);
		};
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(LiowlApplication.class, args);
	}

}
