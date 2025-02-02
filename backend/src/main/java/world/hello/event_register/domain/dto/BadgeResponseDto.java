package world.hello.event_register.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import world.hello.event_register.domain.enums.RegistrationType;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BadgeResponseDto {
    private UUID id;

    private EventDto event;

    private String fullName;

    private String email;

    private RegistrationType registrationType;
}
