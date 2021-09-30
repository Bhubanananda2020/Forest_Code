package com.crts.serviceimpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.crts.entity.MailRequest;
import com.crts.entity.MailResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@Service
public class EmailService {

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private Configuration config;

	public MailResponse sendRequestEmail(String to, Map<String, Object> model) {
		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Template t = config.getTemplate("temp.html");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			helper.setTo(to);
			helper.setText(html, true);
			helper.setSubject("New Request");
			helper.setFrom("cozrequest@gmail.com");
			sender.send(message);
			response.setMessage("mail send to : " + to);
			response.setStatus(Boolean.TRUE);

		} catch (MessagingException | IOException | TemplateException e) {
			response.setMessage("Mail Sending failure : " + e.getMessage());
			response.setStatus(Boolean.FALSE);
		}

		return response;
	}

	
	
	public MailResponse sendResetPasswordEmail(String to, Map<String, Object> model) {
		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Template t = config.getTemplate("tempresetpassword.html");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			helper.setTo(to);
			helper.setText(html, true);
			helper.setSubject("Password Reset !!");
			helper.setFrom("cozrequest@gmail.com");
			sender.send(message);
			response.setMessage("mail send to : " + to);
			response.setStatus(Boolean.TRUE);

		} catch (MessagingException | IOException | TemplateException e) {
			response.setMessage("Mail Sending failure : " + e.getMessage());
			response.setStatus(Boolean.FALSE);
		}
		return response;
	}

	
	
	public MailResponse sendNewUserEmail(String to, Map<String, Object> model) {
		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Template t = config.getTemplate("tempusercreate.html");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			helper.setTo(to);
			helper.setText(html, true);
			helper.setSubject("New User Create !!");
			helper.setFrom("cozrequest@gmail.com");
			sender.send(message);
			response.setMessage("mail send to : " + to);
			response.setStatus(Boolean.TRUE);

		} catch (MessagingException | IOException | TemplateException e) {
			response.setMessage("Mail Sending failure : " + e.getMessage());
			response.setStatus(Boolean.FALSE);
		}
		return response;
	}

	
	
	
}
