package world.hello.event_register.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import world.hello.event_register.service.MailService;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

  @Value("${spring.mail.username}")
  private String fromEmail;

  private final JavaMailSender mailSender;

  public MailServiceImpl(final JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Async
  @Override
  public void sendMail(final String to, final String subject, final String text) {
    try {
      log.info("Preparing to send simple email to: {}", to);
      // Create SimpleMailMessage and set the email properties
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(to);
      message.setFrom(fromEmail);
      message.setSubject(subject);
      message.setText(text);
      mailSender.send(message);
      log.info("Simple email successfully sent to {}", to);
    } catch (Exception ex) {
      log.error("Error while sending simple mail to {}: {}", to, ex.getMessage());
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Error while sending mail.", ex);
    }
  }

  @Override
  @Async
  public void sendMailWithAttachment(
      final String to, final String subject, final String text, final byte[] attachment) {
    try {
      log.info("Preparing to send email with attachment to: {}", to);
      // Create MimeMessage and MimeMessageHelper for advanced email features
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      // Set the basic email properties
      helper.setTo(to);
      helper.setFrom(fromEmail);
      helper.setSubject(subject);
      helper.setText(text);
      // Add the PDF attachment if it's not null or empty
      if (attachment != null && attachment.length > 0) {
        helper.addAttachment("badge.pdf", new ByteArrayDataSource(attachment, "application/pdf"));
        log.info("Attachment added successfully to email for {}", to);
      } else {
        log.warn("No attachment provided for email to {}", to);
      }
      // Send the email
      mailSender.send(message);
      log.info("Email with attachment successfully sent to {}", to);
    } catch (Exception ex) {
      log.error("Error while sending email with attachment to {}: {}", to, ex.getMessage());
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Error while sending mail with attachment.", ex);
    }
  }
}