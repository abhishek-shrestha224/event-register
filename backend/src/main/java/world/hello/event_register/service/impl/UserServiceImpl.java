package world.hello.event_register.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import world.hello.event_register.domain.dto.UserCreateDto;
import world.hello.event_register.domain.dto.UserDto;
import world.hello.event_register.domain.entity.UserEntity;
import world.hello.event_register.exception.NotFoundException;
import world.hello.event_register.repository.UserRepository;
import world.hello.event_register.service.UserService;
import world.hello.event_register.utils.mapper.UserMapper;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  static final String NOT_FOUND = "User Not Found";
  static final String CATCH_ALL_ERRORS = "Something Went wrong";

  public UserServiceImpl(final UserRepository userRepository, final UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public UserDto registerUser(UserCreateDto createData) {
    log.info("Creating user with data: {}", createData);
    try {
      final UserEntity savedUser = userRepository.save(userMapper.toEntity(createData));
      log.debug("Saved user: {}", savedUser);

      log.info("Successfully created user with ID: {}", savedUser.getId());
      return userMapper.toDto(savedUser);

    } catch (DataIntegrityViolationException ex) {
      log.error("Some fields violate the data integrity constraints", ex);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Data Integrity Violation", ex);

    } catch (TransactionSystemException ex) {
      log.error("Transaction System error occurred while creating user", ex);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "An exception occurred in transaction", ex);
    } catch (Exception ex) {
      log.error(CATCH_ALL_ERRORS, ex);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, CATCH_ALL_ERRORS, ex);
    }
  }

  @Override
  public boolean doesUserExist(String email) {
    log.info("Checking if user exists with email: {}", email);
    try {
      final UserEntity user = userRepository.findUserWithoutBadgesByEmail(email).orElse(null);
      log.info("Found user with email: {}", user != null ? user : NOT_FOUND);
      return user != null;
    } catch (Exception ex) {
      log.error(CATCH_ALL_ERRORS, ex);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, CATCH_ALL_ERRORS, ex);
    }
  }

  @Override
  @Transactional
  public UserDto getUserByEmail(String email) {
    try {
      final UserEntity user =
          userRepository
              .findUserWithoutBadgesByEmail(email)
              .orElseThrow(() -> new NotFoundException(NOT_FOUND));
      return userMapper.toDto(user);
    } catch (NotFoundException ex) {
      log.error(NOT_FOUND, ex);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND, ex);
    } catch (Exception ex) {
      log.error(CATCH_ALL_ERRORS, ex);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, CATCH_ALL_ERRORS, ex);
    }
  }

  @Override
  public UserEntity getUserEntityByEmail(String email) {
    try {
      return userRepository
          .findUserWithBadgesByEmail(email)
          .orElseThrow(() -> new NotFoundException(NOT_FOUND));
    } catch (NotFoundException ex) {
      log.error(NOT_FOUND, ex);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND, ex);
    } catch (Exception ex) {
      log.error(CATCH_ALL_ERRORS, ex);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, CATCH_ALL_ERRORS, ex);
    }
  }
}