package world.hello.event_register.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import world.hello.event_register.domain.dto.EventDto;
import world.hello.event_register.domain.entity.EventEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends CrudRepository<EventEntity, UUID> {
    @Query("SELECT new world.hello.event_register.domain.dto.EventDto(e.id, e.name, e.venue, e.eventDate) " +
            "FROM events e WHERE e.id = :id")
    Optional<EventDto> findEventDtoById(UUID id);

    @Query("SELECT new world.hello.event_register.domain.dto.EventDto(e.id, e.name, e.venue, e.eventDate) " +
            "FROM events e")
    List<EventDto> findAllEventDto();

    @Query("SELECT e FROM events e LEFT JOIN FETCH e.badges WHERE e.id = :id")
    Optional<EventEntity> findEventEntityById(UUID id);
}
