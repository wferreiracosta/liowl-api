package com.wferreiracosta.liowl.service;

import java.util.List;

public interface EmailService {

	void sendMails(String message, List<String> mailsList);

}
