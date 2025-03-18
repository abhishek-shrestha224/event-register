package world.hello.event_register.utils.validation;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class SlugValidation {

  private static final Pattern emailPattern =
      Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

  private SlugValidation() {}

  public static boolean emailValidationFails(String email) {
    if (email == null || email.isEmpty()) {
      return true;
    }
    return !(emailPattern.matcher(email).matches());
  }

  public static boolean uuidValidationFails(UUID id) {
    return id == null || !isValidUUID(id);
  }

  private static boolean isValidUUID(UUID id) {
    try {
      UUID.fromString(id.toString()); // Tries to convert the UUID to a string representation
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static boolean fileValidationFails(MultipartFile file) {
    return (file == null || file.isEmpty());
  }
}