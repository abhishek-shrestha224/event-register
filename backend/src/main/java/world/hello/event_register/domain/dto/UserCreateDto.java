package world.hello.event_register.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDto {

  @NotBlank private String firstName;

  @NotBlank private String lastName;

  @Email private String email;

  @NotBlank private String phoneNumber;
}