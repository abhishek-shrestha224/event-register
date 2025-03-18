package world.hello.event_register.utils.mapper.impl;

import org.springframework.stereotype.Component;
import world.hello.event_register.domain.dto.EventCreateDto;
import world.hello.event_register.domain.dto.EventDto;
import world.hello.event_register.domain.entity.EventEntity;
import world.hello.event_register.utils.mapper.EventMapper;

import java.time.LocalDateTime;

@Component
public class EventMapperImpl implements EventMapper {

  @Override
  public EventDto toDto(EventEntity entity) {
    return EventDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .venue(entity.getVenue())
        .eventDate(entity.getEventDate())
        .build();
  }

  @Override
  public EventEntity toEntity(EventCreateDto dto) {
    return EventEntity.builder()
        .id(null)
        .name(dto.getName())
        .venue(dto.getVenue())
        .eventDate(dto.getEventDate())
        .badges(null)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }
}