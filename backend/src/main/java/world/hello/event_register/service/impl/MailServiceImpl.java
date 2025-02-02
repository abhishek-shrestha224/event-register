package world.hello.event_register.service.impl;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import world.hello.event_register.service.MailService;

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

        try{
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setFrom(fromEmail);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
        }catch(Exception ex){
            log.error("Error while sending mail. {}",ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error while sending mail.", ex);
        }
    }
}
