package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Month;

public class AfterCinemaBirthdayValidator implements ConstraintValidator<AfterCinemaBirthday, LocalDate> {
    private final LocalDate cinemaBirthday = LocalDate.of(1895, Month.DECEMBER, 28);

    public final void initialize(final AfterCinemaBirthday annotation) {
    }

    public final boolean isValid(final LocalDate value,
                                 final ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return cinemaBirthday.isBefore(value);
    }
}