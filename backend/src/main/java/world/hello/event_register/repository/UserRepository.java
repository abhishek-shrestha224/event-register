package world.hello.event_register.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import world.hello.event_register.domain.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {
    @Query("SELECT u FROM users u LEFT JOIN FETCH u.badges WHERE u.email = :email")
    Optional<UserEntity> findUserWithBadgesByEmail(String email);

    // Query to fetch a UserEntity without its associated Badges
    @Query("SELECT u FROM users u WHERE u.email = :email")
    Optional<UserEntity> findUserWithoutBadgesByEmail(String email);
}
