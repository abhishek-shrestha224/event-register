package world.hello.event_register.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import world.hello.event_register.domain.enums.RegistrationType;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BadgeDto {
    private UUID id;

    private EventDto event;

    private String userEmail;

    private String photoPath;

    private RegistrationType registrationType;
}
