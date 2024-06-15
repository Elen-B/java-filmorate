package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({ FIELD, METHOD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AfterCinemaBirthdayValidator.class)
@Documented
public @interface AfterCinemaBirthday {
    String message() default "The date is before cinema birthday";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
