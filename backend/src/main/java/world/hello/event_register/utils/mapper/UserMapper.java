package world.hello.event_register.utils.mapper;

import world.hello.event_register.domain.dto.UserCreateDto;
import world.hello.event_register.domain.dto.UserDto;
import world.hello.event_register.domain.entity.UserEntity;

public interface UserMapper {

    UserDto toDto(UserEntity user);

    UserEntity toEntity(UserCreateDto dto);
}
