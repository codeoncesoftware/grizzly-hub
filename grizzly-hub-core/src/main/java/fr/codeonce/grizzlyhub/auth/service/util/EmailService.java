package fr.codeonce.grizzlyhub.auth.service.util;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.from}")
	private String userFrom;
	@Value("${spring.mail.personal}")
	private String personal;

	public boolean send(String content, String subject, String email) {
		log.info("send email with subject: {} to email: {}", subject, email);
		
		MimeMessage message = mailSender.createMimeMessage();
		try {

			MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
			helper.setFrom(userFrom,personal);
			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(content, true);
			message.setContent(content, "text/html; charset=utf-8");
			mailSender.send(message);
			return true;
		} catch (Exception e) {
			log.error("could not send email", e);
		}
		return false;
	}

}
