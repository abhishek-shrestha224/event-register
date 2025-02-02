package world.hello.event_register.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.hello.event_register.service.MailService;

@RestController
@RequestMapping("/mail")
public class MailController {
    private final MailService mailService;

    public MailController(final MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send-with-attachment")
    public String sendWithAttachment() {
        mailService.sendMail("abhishekshrestha416@gmail.com", "Test Mail", "Mail sent from Spring Boot.");
        return "Hi";
    }
}
