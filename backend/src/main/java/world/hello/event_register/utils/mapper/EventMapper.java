package world.hello.event_register.utils.mapper;


import world.hello.event_register.domain.dto.EventCreateDto;
import world.hello.event_register.domain.dto.EventDto;
import world.hello.event_register.domain.entity.EventEntity;

public interface EventMapper {
    EventDto toDto(EventEntity entity);

    EventEntity toEntity(EventCreateDto dto);
}
