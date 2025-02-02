package world.hello.event_register.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import world.hello.event_register.domain.dto.BadgeDto;
import world.hello.event_register.domain.dto.UserDto;
import world.hello.event_register.domain.entity.BadgeEntity;
import world.hello.event_register.domain.enums.RegistrationType;
import world.hello.event_register.exception.GenericException;
import world.hello.event_register.exception.InvalidFileException;
import world.hello.event_register.exception.NotFoundException;
import world.hello.event_register.repository.BadgeRepository;
import world.hello.event_register.service.*;
import world.hello.event_register.utils.mapper.BadgeMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BadgeServiceImpl implements BadgeService {
    private final PdfService pdfService;
    private final BadgeRepository badgeRepository;
    private final UserService userService;
    private final EventService eventService;
    private final BadgeMapper badgeMapper;
    private final FileService fileService;


    public BadgeServiceImpl(
            final BadgeRepository badgeRepository,
            final BadgeMapper badgeMapper,
            final UserService userService,
            final EventService eventService,
            final FileService fileService,
            final PdfService pdfService
    ) {
        this.badgeRepository = badgeRepository;
        this.badgeMapper = badgeMapper;
        this.userService = userService;
        this.eventService = eventService;
        this.fileService = fileService;
        this.pdfService = pdfService;

    }

    @Override
    public BadgeDto createBadge(RegistrationType registrationType, String userEmail, UUID eventId, MultipartFile photo) {
        try {
            log.info("Creating badge for user: {}, event: {}, registrationType: {}", userEmail, eventId, registrationType);

            if (!userService.doesUserExist(userEmail)) {
                log.error("User not found with email: {}", userEmail);
                throw new NotFoundException("User not found");
            }

            if (!eventService.doesEventExist(eventId)) {
                log.error("Event not found with id: {}", eventId);
                throw new NotFoundException("Event not found");
            }


            if (isBadgeDuplicate(userEmail, eventId)) {
                log.error("Duplicate badge detected for user: {} and event: {}", userEmail, eventId);
                throw new GenericException("Duplicate badge");
            }

            photo = fileService.validateFile(photo);
            String photoPath = fileService.uploadFile(photo);

            final BadgeEntity badge = BadgeEntity.builder()
                    .id(null)
                    .registrationType(registrationType)
                    .user(userService.getUserEntityByEmail(userEmail))
                    .event(eventService.getEventEntityById(eventId))
                    .photoPath(photoPath)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            BadgeEntity savedBadge = badgeRepository.save(badge);
            log.info("Badge successfully created with ID: {}", savedBadge.getId());
            return badgeMapper.toDto(savedBadge);

        } catch (DataIntegrityViolationException ex) {
            log.error("Data integrity violation occurred while creating badge for user: {}, event: {}. Details: {}", userEmail, eventId, ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data integrity violation", ex);

        } catch (GenericException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
        } catch (TransactionSystemException ex) {
            log.error("Transaction system exception occurred while creating badge for user: {}, event: {}. Details: {}", userEmail, eventId, ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction failed", ex);
        } catch (InvalidFileException ex) {
            log.error("Unexpected error occurred while writing file: {}, event: {}. Exception: {}", userEmail, eventId, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error occurred while creating badge for user: {}, event: {}. Exception: {}", userEmail, eventId, ex.getClass().getName(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction failed", ex);
        }
    }

    private boolean isBadgeDuplicate(String userEmail, UUID eventId) {
        BadgeEntity existingBadge = badgeRepository.findByUser_EmailAndEvent_Id(userEmail, eventId).orElse(null);
        return existingBadge != null;
    }

    @Override
    public List<BadgeDto> getBadgesByUserEmail(String userEmail) {
        log.info("Getting Badges by email {}", userEmail);
        try {
            List<BadgeEntity> badges = badgeRepository.findByUser_Email(userEmail);
            return badges.stream()
                    .map(badgeMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error occurred while fetching badges", ex);
            throw new GenericException("Failed to fetch badges", ex);
        }

    }

    @Override
    public BadgeDto getBadgeByUserEmailAndId(String userEmail, UUID id) {
        log.info("Fetching badge by email{} and id {}", userEmail, id);
        try {
            BadgeEntity badge = badgeRepository.findByUser_EmailAndId(userEmail, id)
                    .orElseThrow(() -> new NotFoundException("Badge not found"));
            log.info("Badge by email: {} and id: {}", userEmail, id);
            return badgeMapper.toDto(badge);
        } catch (NotFoundException ex) {
            log.error("Badge with ID {} not found", id, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Error occurred while fetching badge with ID {}", id, ex);
            throw new GenericException("Failed to fetch badge", ex);
        }
    }
    @Override
    public byte[] createBadgePdf(String userEmail, UUID badgeId) {
        try {
            log.info("Received request to create PDF for badge ID: {} and user email: {}", badgeId, userEmail);
            // Fetch user details using the associated user email
            final UserDto user = userService.getUserByEmail(userEmail);
            if (user == null) {
                log.warn("User not found for email: {}", userEmail);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            // Fetch the badge details using the badge ID
            final BadgeDto badge = getBadgeByUserEmailAndId(userEmail, badgeId);
            if (badge == null) {
                log.warn("Badge not found for ID: {}", badgeId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Badge not found");
            }
            // Prepare badge data for PDF generation
            final Map<String, Object> badgeData = new HashMap<>();
            badgeData.put("eventName", badge.getEvent().getName());
            badgeData.put("eventVenue", badge.getEvent().getVenue());
            badgeData.put("eventDate", badge.getEvent().getEventDate());
            badgeData.put("userFullName", user.getFirstName() + " " + user.getLastName());
            badgeData.put("userEmail", user.getEmail());
            badgeData.put("badgeType", badge.getRegistrationType());
            // Generate the PDF for the badge
            byte[] pdfBytes = pdfService.generatePdf(badgeData, badge.getPhotoPath());
            log.info("PDF successfully generated for badge ID: {}", badgeId);
            return pdfBytes;
        } catch (ResponseStatusException ex) {
            log.error(ex.getMessage(), badgeId, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Error generating PDF for badge ID: {}", badgeId, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating PDF");
        }
    }

}
