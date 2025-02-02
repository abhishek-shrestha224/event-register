package world.hello.event_register.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.server.ResponseStatusException;
import world.hello.event_register.domain.dto.EventCreateDto;
import world.hello.event_register.domain.dto.EventDto;
import world.hello.event_register.domain.entity.EventEntity;
import world.hello.event_register.exception.GenericException;
import world.hello.event_register.exception.NotFoundException;
import world.hello.event_register.repository.EventRepository;
import world.hello.event_register.service.EventService;
import world.hello.event_register.utils.mapper.EventMapper;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventServiceImpl(final EventRepository eventRepository, final EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }


    @Override
    public EventDto createEvent(EventCreateDto createData) {
        log.info("Creating event with data: {}", createData);
        try {
            // Convert the DTO to Entity
            final EventEntity eventEntity = eventMapper.toEntity(createData);
            log.debug("Mapped EventCreatDto to EventEntity: {}", eventEntity);

            // Save the entity in the database
            final EventEntity savedEvent = eventRepository.save(eventEntity);
            log.debug("Saved event: {}", savedEvent);

            log.info("Successfully created event with ID: {}", savedEvent.getId());
            // Convert saved entity back to DTO
            return eventMapper.toDto(savedEvent);

        } catch (DataIntegrityViolationException ex) {
            log.error("Some fields violate the data integrity constraints", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Data Integrity Violation", ex);

        } catch (TransactionSystemException ex) {
            log.error("Transaction System error occurred while creating event", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An exception occurred in transaction", ex);
        } catch (Exception ex) {
            log.error("An unexpected error occurred while creating event", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something Went Wrong", ex);
        }
    }


    @Override
    public List<EventDto> getAllEvents() {
        log.info("Fetching all events");
        try {
            return eventRepository.findAllEventDto();
        } catch (Exception ex) {
            log.error("Error occurred while fetching events", ex);
            throw new GenericException("Failed to fetch events", ex);
        }
    }

    @Override
    public boolean doesEventExist(final UUID id) {
        log.info("Fetching event with ID: {}", id);
        try {
            final EventDto event = eventRepository.findEventDtoById(id).orElse(null);
            log.info("Found user with email: {}", event != null ? event : "Not found");
            return event != null;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while checking if event exists", ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something Went Wrong", ex);
        }
    }

    @Override
    public EventDto getEventById(UUID id) {
        log.info("Fetching event with ID: {}", id);
        try {
            final EventDto event = eventRepository.findEventDtoById(id)
                    .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));
            log.info("Found event: {}", event);
            return event;
        } catch (NotFoundException ex) {
            log.error("Event with ID {} not found", id, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Error occurred while fetching event with ID {}", id, ex);
            throw new GenericException("Failed to fetch events", ex);
        }
    }

    @Override
    public EventEntity getEventEntityById(UUID id) {
        log.info("Fetching event with ID: {}", id);
        try {
            return eventRepository.findEventEntityById(id)
                    .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));
        } catch (NotFoundException ex) {
            log.error("Event with ID {} not found", id, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Error occurred while fetching event with ID {}", id, ex);
            throw new GenericException("Failed to fetch events", ex);
        }
    }
}
