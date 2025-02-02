package world.hello.event_register.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import world.hello.event_register.domain.dto.BadgeDto;
import world.hello.event_register.domain.dto.BadgePdfResponse;
import world.hello.event_register.domain.dto.BadgeResponseDto;
import world.hello.event_register.domain.dto.UserDto;
import world.hello.event_register.service.BadgeService;
import world.hello.event_register.service.UserService;
import world.hello.event_register.utils.validation.SlugValidation;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/badges")
@Slf4j
public class BadgeController {
    private final BadgeService badgeService;
    private final UserService userService;

    public BadgeController(final BadgeService badgeService, final UserService userService) {
        this.badgeService = badgeService;
        this.userService = userService;
    }

    //     *Get List of BadgeDto based on user email send in header.
    @GetMapping
    public ResponseEntity<List<BadgeDto>> getBadgesByUser(@RequestHeader(value = "X-User-Email") final String email) {
        log.info("Received request to get badges for user with email: {}", email);
        // Validate email from the cookie
        if (SlugValidation.emailValidationFails(email)) {
            log.warn("Invalid email found in cookie: {}", email);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid email cookie");
        }
        // Fetch badges for the user by email
        final List<BadgeDto> badges = badgeService.getBadgesByUserEmail(email);
        log.info("Found {} badges for user with email: {}", badges.size(), email);
        // Return the badges in the response
        return ResponseEntity.status(HttpStatus.OK).body(badges);
    }

//     * Get BadgeDto along with pdf of the badge based on email and badge id
    @GetMapping("/{id}")
    public ResponseEntity<BadgePdfResponse> getBadgeByIdAndPdf(
            @RequestHeader(value = "X-User-Email") final String email,
            @PathVariable("id") final UUID id
    ) {
        log.info("Received request to get badge by userEmail: {} and badgeId: {}", email, id);
        // Validate email from the header
        if (SlugValidation.emailValidationFails(email)) {
            log.warn("Invalid email provided: {}", email);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid email header");
        }
        // Validate the badge ID in the path parameter
        if (SlugValidation.uuidValidationFails(id)) {
            log.warn("Invalid badge ID provided: {}", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path parameter");
        }
        final byte[] pdfBytes = badgeService.createBadgePdf(email, id);
        final UserDto user = userService.getUserByEmail(email);
        final BadgeDto badge = badgeService.getBadgeByUserEmailAndId(email, id);
        // Prepare the response with both badge and PDF
        final BadgeResponseDto badgeResponseDto = BadgeResponseDto.builder()
                .id(badge.getId())
                .event(badge.getEvent())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .registrationType(badge.getRegistrationType())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=badge-" + id + ".pdf");
        // Return the BadgeDto as JSON and the PDF as binary content
        return ResponseEntity.ok()
                .headers(headers)
                .body(new BadgePdfResponse(badgeResponseDto, pdfBytes));
    }
}
