package world.hello.event_register.utils.mapper;

import world.hello.event_register.domain.dto.BadgeDto;
import world.hello.event_register.domain.entity.BadgeEntity;

public interface BadgeMapper {

  BadgeDto toDto(BadgeEntity entity);
}