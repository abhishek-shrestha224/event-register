package world.hello.event_register.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import world.hello.event_register.domain.entity.BadgeEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BadgeRepository extends CrudRepository<BadgeEntity, UUID> {

  Optional<BadgeEntity> findByUser_EmailAndEvent_Id(String userEmail, UUID eventId);

  List<BadgeEntity> findByUser_Email(String userEmail);

  Optional<BadgeEntity> findByUser_EmailAndId(String userEmail, UUID id);
}