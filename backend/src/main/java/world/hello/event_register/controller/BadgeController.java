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
import world.hello.event_register.service.PdfService;
import world.hello.event_register.service.UserService;
import world.hello.event_register.utils.validation.SlugValidation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/badges")
@Slf4j
public class BadgeController {

    private final BadgeService badgeService;
    private final PdfService pdfService;
    private final UserService userService;


    public BadgeController(final BadgeService badgeService, final PdfService pdfService, final UserService userService) {
        this.badgeService = badgeService;
        this.pdfService = pdfService;
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

        // Fetch badge details for the user and badge ID
        final BadgeDto badge = badgeService.getBadgeByUserEmailAndId(email, id);
        log.info("Found badge for user {} with ID {}: {}", email, id, badge);

        final UserDto user = userService.getUserByEmail(email);

        final Map<String, Object> badgeData = new HashMap<>();

        badgeData.put("eventName", badge.getEvent().getName());
        badgeData.put("eventVenue", badge.getEvent().getVenue());
        badgeData.put("eventDate", badge.getEvent().getEventDate());
        badgeData.put("userFullName", user.getFirstName() + " " + user.getLastName());
        badgeData.put("userEmail", user.getEmail());
        badgeData.put("badgeType", badge.getRegistrationType());
        // Generate the PDF for the badge
        byte[] pdfBytes;
        try {
            pdfBytes = pdfService.generatePdf(badgeData, badge.getPhotoPath());  // Assuming you have a method to generate the PDF
            log.info("PDF successfully generated for badge ID: {}", id);
        } catch (Exception ex) {
            log.error("Error generating PDF for badge ID: {}", id, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating PDF");
        }

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
