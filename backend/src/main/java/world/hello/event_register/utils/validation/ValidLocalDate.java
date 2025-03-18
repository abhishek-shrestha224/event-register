package world.hello.event_register.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateValidator.class)
public @interface ValidLocalDate {
  String message() default "Event date cannot be in the past";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean isAfterCurrentTime() default true;
}