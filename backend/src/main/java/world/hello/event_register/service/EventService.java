package world.hello.event_register.service;

import world.hello.event_register.domain.dto.EventCreateDto;
import world.hello.event_register.domain.dto.EventDto;
import world.hello.event_register.domain.entity.EventEntity;

import java.util.List;
import java.util.UUID;

public interface EventService {
    EventDto createEvent(EventCreateDto createData);

    List<EventDto> getAllEvents();

    EventDto getEventById(UUID id);

    boolean doesEventExist(UUID id);

    EventEntity getEventEntityById(UUID id);
}
