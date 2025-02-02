package world.hello.event_register.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BadgePdfResponse {
    private BadgeResponseDto badge;
    private byte[] pdf;
}
