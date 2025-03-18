package world.hello.event_register.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import world.hello.event_register.service.MailService;

@RestController
public class EntryController {
  private final MailService mailService;

  public EntryController(final MailService mailService) {
    this.mailService = mailService;
  }

  @GetMapping
  public String hello() {
    return "Hello World";
  }

  @GetMapping("/mail")
  public String mail() {
    mailService.sendMail("abhishekshrestha416@gmail.com", "Test Mail", "Hello World");

    return "Mail Sent";
  }
}