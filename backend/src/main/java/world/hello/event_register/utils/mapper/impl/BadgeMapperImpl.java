package world.hello.event_register.utils.mapper.impl;

import org.springframework.stereotype.Component;
import world.hello.event_register.domain.dto.BadgeDto;
import world.hello.event_register.domain.entity.BadgeEntity;
import world.hello.event_register.utils.mapper.BadgeMapper;
import world.hello.event_register.utils.mapper.EventMapper;

@Component
public class BadgeMapperImpl implements BadgeMapper {
    private final EventMapper eventMapper;

    public BadgeMapperImpl(final EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    @Override
    public BadgeDto toDto(BadgeEntity entity) {
        return BadgeDto.builder()
                .id(entity.getId())
                .event(eventMapper.toDto(entity.getEvent()))
                .userEmail(entity.getUser().getEmail())
                .photoPath(entity.getPhotoPath())
                .registrationType(entity.getRegistrationType())
                .build();
    }
}
