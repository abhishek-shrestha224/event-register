package world.hello.event_register.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import world.hello.event_register.domain.dto.BadgeDto;
import world.hello.event_register.domain.dto.EventCreateDto;
import world.hello.event_register.domain.dto.EventDto;
import world.hello.event_register.domain.dto.FormDataDto;
import world.hello.event_register.service.BadgeService;
import world.hello.event_register.service.EventService;
import world.hello.event_register.service.MailService;
import world.hello.event_register.utils.validation.SlugValidation;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@Slf4j
public class EventController {
    private final EventService eventService;
    private final BadgeService badgeService;
    private final MailService mailService;

    @Autowired
    public EventController(
            final EventService eventService,
            final BadgeService badgeService,
            final MailService mailService
    ) {
        this.eventService = eventService;
        this.badgeService = badgeService;
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody @Valid final EventCreateDto createData) {
        log.info("Creating event {}", createData);
        final EventDto eventDto = eventService.createEvent(createData);
        log.info("Event created DTO: {}", eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventDto);
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        log.info("Getting all events");
        final List<EventDto> eventDtoList = eventService.getAllEvents();
        log.info("Event list: {}", eventDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(eventDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable("id") final UUID id) {
        if (SlugValidation.uuidValidationFails(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path parameter");
        }
        log.info("Getting event by id: {}", id);
        final EventDto eventDto = eventService.getEventById(id);
        log.info("Event found {}", eventDto);
        return ResponseEntity.status(HttpStatus.OK).body(eventDto);
    }

    @PostMapping(value = "/{id}/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, UUID>> registerUserToEvent(
            @PathVariable("id") UUID eventId,
            @RequestHeader(value = "X-User-Email") final String userEmail,
            @ModelAttribute @Valid FormDataDto formData) {
        if (SlugValidation.uuidValidationFails(eventId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path parameter");
        }
        log.info("Registering to Event of id: {}", eventId);
        if (SlugValidation.emailValidationFails(userEmail)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid email cookie");
        }
        log.info("Registering the user of email: {}", userEmail);
        if (SlugValidation.fileValidationFails(formData.getPhoto())) {
            log.warn("No photo");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "File not attached with request");
        }
        MultipartFile photo = formData.getPhoto();
        final BadgeDto savedBadge = badgeService.createBadge(formData.getRegistrationType(), userEmail, eventId, photo);
        final byte[] badgePdf = badgeService.createBadgePdf(userEmail, savedBadge.getId());
        mailService.sendMailWithAttachment("abhishekshrestha416@gmail.com", "Event Badge Created", "The badge for the event is attached with this email.", badgePdf);
        final Map<String, UUID> res = new HashMap<>();
        res.put("id", savedBadge.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
