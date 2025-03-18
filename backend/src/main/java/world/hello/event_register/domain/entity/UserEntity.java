package world.hello.event_register.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "pg-uuid")
  @Column(unique = true, nullable = false, updatable = false, name = "user_id")
  private UUID id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false, updatable = false, unique = true)
  private String email;

  @Column(nullable = false, length = 10, unique = true)
  private String phoneNumber;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "user",
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
      orphanRemoval = true)
  @JsonManagedReference
  private List<BadgeEntity> badges;

  @Column(updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;
}