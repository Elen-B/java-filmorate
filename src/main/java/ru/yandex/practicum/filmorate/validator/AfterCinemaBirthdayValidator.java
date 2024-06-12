package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.util.Calendar;

public class AfterCinemaBirthdayValidator implements ConstraintValidator<AfterCinemaBirthday, Instant> {
    private final Calendar cinemaBirthday;

    public AfterCinemaBirthdayValidator() {
        cinemaBirthday = Calendar.getInstance();
        cinemaBirthday.set(1895, Calendar.DECEMBER, 28);
    }

    public final void initialize(final AfterCinemaBirthday annotation) {
    }

    public final boolean isValid(final Instant value,
                                 final ConstraintValidatorContext context) {
        return value.getEpochSecond() - cinemaBirthday.toInstant().getEpochSecond() >= 0;
    }
}