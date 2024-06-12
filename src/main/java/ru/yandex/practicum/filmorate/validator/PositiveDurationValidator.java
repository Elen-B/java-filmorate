package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class PositiveDurationValidator  implements ConstraintValidator<PositiveDuration, Duration> {

    public final void initialize(final PositiveDuration annotation) {
    }

    public final boolean isValid(final Duration value,
                                 final ConstraintValidatorContext context) {
        return value.isPositive();
    }
}