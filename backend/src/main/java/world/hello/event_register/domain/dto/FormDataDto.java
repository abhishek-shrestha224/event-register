package world.hello.event_register.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import world.hello.event_register.domain.enums.RegistrationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormDataDto {
  private RegistrationType registrationType;

  private MultipartFile photo;
}