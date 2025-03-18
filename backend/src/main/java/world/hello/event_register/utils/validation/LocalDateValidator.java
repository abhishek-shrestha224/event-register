package world.hello.event_register.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class LocalDateValidator implements ConstraintValidator<ValidLocalDate, LocalDate> {
  private boolean isAfterCurrentTime;

  @Override
  public void initialize(ValidLocalDate constraintAnnotation) {
    this.isAfterCurrentTime = constraintAnnotation.isAfterCurrentTime();
  }

  @Override
  public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
    if (isAfterCurrentTime) {
      return !value.isBefore(LocalDate.now());
    }

    return value != null;
  }
}