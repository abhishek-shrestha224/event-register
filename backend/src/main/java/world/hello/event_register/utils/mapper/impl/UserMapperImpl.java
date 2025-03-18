package world.hello.event_register.utils.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import world.hello.event_register.domain.dto.UserCreateDto;
import world.hello.event_register.domain.dto.UserDto;
import world.hello.event_register.domain.entity.UserEntity;
import world.hello.event_register.utils.mapper.BadgeMapper;
import world.hello.event_register.utils.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {
  private final BadgeMapper badgeMapper;

  @Autowired
  public UserMapperImpl(final BadgeMapper badgeMapper) {
    this.badgeMapper = badgeMapper;
  }

  @Override
  public UserEntity toEntity(UserCreateDto dto) {
    return UserEntity.builder()
        .id(null)
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .email(dto.getEmail())
        .phoneNumber(dto.getPhoneNumber())
        .badges(null)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  @Override
  public UserDto toDto(UserEntity entity) {
    return UserDto.builder()
        .firstName(entity.getFirstName())
        .lastName(entity.getLastName())
        .email(entity.getEmail())
        .phoneNumber(entity.getPhoneNumber())
        .badges(
            Optional.ofNullable(entity.getBadges())
                .map(badges -> badges.stream().map(badgeMapper::toDto).collect(Collectors.toList()))
                .orElse(null))
        .build();
  }
}