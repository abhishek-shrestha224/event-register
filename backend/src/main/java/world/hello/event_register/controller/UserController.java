package world.hello.event_register.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import world.hello.event_register.domain.dto.UserCreateDto;
import world.hello.event_register.domain.dto.UserDto;
import world.hello.event_register.service.UserService;
import world.hello.event_register.utils.validation.SlugValidation;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;


    public UserController(final UserService userService) {
        this.userService = userService;

    }

    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserCreateDto user) {
        log.info("Received JSON: {}", user);
        final UserDto created = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/check")
    public ResponseEntity<UserDto> getUser(@RequestHeader(value = "X-User-Email") final String email) {
        log.info("Received email: {}", email);
        if (SlugValidation.emailValidationFails(email)) {
            log.warn("Email validation failed");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid email cookie");
        }

        final UserDto found = userService.getUserByEmail(email);
        log.info("Found user: {}", found);
        return ResponseEntity.status(HttpStatus.OK).body(found);
    }
}
