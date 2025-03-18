package world.hello.event_register.service;

import world.hello.event_register.domain.dto.UserCreateDto;
import world.hello.event_register.domain.dto.UserDto;
import world.hello.event_register.domain.entity.UserEntity;

public interface UserService {
  UserDto registerUser(UserCreateDto userDto);

  boolean doesUserExist(String email);

  UserDto getUserByEmail(String email);

  UserEntity getUserEntityByEmail(String email);
}