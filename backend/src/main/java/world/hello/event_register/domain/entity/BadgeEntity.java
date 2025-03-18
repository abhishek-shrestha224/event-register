package world.hello.event_register.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import world.hello.event_register.domain.enums.RegistrationType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "badges")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "badges")
public class BadgeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, updatable = false, unique = true, name = "badge_id")
  @Type(type = "pg-uuid")
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, updatable = false)
  private RegistrationType registrationType;

  @Column(nullable = false)
  private String photoPath;

  @ManyToOne()
  @JoinColumn(name = "user_id", nullable = false, updatable = false)
  @JsonBackReference
  private UserEntity user;

  @ManyToOne()
  @JoinColumn(name = "event_id", nullable = false, updatable = false)
  @JsonBackReference
  private EventEntity event;

  @Column(updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;
}