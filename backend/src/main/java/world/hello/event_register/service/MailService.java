package world.hello.event_register.service;

public interface MailService {
    void sendMail(final String to, final String subject, final String text);
}
