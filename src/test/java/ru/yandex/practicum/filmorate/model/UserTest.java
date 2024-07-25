package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUser() {
        Long id = 1L;
        String email = "mail@mail.org";
        String login = "user1";
        String name = "пользователь 1";
        LocalDate birthday = LocalDate.of(1990, Month.MARCH, 1);
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);

        assertNotNull(user, "Пользователь не создан");
        assertEquals(id, user.getId(), "Неверный ид созданного пользователя");
        assertEquals(name, user.getName(), "Неверное имя созданного пользователя");
        assertEquals(email, user.getEmail(), "Неверный email созданного пользователя");
        assertEquals(login, user.getLogin(), "Неверный логин созданного пользователя");
        assertEquals(birthday, user.getBirthday(), "Неверная дата рождения созданного пользователя");
    }

    @Test
    void shouldSetLoginValueForEmptyName() {
        User user = buildValidTestUser();
        user.setName(null);

        assertEquals(user.getLogin(), user.getName(), "Имя пользователя должно быть таким же, как Login");
    }

    @Test
    void shouldNotValidUserWithEmptyLogin() {
        User user = buildValidTestUser();
        user.setLogin(null);

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            var violations = validator.validate(user);
            assertEquals(1, violations.stream()
                    .toList().size(), "Валидация не выполнена");
            assertEquals("NotBlank",
                    violations.stream()
                            .toList()
                            .getFirst()
                            .getConstraintDescriptor()
                            .getAnnotation().annotationType().getSimpleName(),
                    "Не работает валидация на пустой логин пользователя");
        }
    }

    @Test
    void shouldNotValidUserWithWrongEmail() {
        User user = buildValidTestUser();
        user.setEmail("qwerty");

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            var violations = validator.validate(user);
            assertEquals(1, violations.stream()
                    .toList().size(), "Валидация не выполнена");
            assertEquals("Email",
                    violations.stream()
                            .toList()
                            .getFirst()
                            .getConstraintDescriptor()
                            .getAnnotation().annotationType().getSimpleName(),
                    "Не работает валидация на некорректную почту пользователя");
        }
    }

    @Test
    void shouldNotValidUserWithNullEmail() {
        User user = buildValidTestUser();
        user.setEmail(null);

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            var violations = validator.validate(user);
            assertEquals(2, violations.stream()
                    .toList().size(), "Валидация не выполнена");
            assertTrue(
                    violations.stream()
                            .anyMatch(cf -> cf.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("NotBlank")),
                    "Не работает валидация на пустую почту пользователя");
        }
    }

    @Test
    void shouldNotValidUserWithFutureBirthday() {
        User user = buildValidTestUser();
        user.setBirthday(LocalDate.of(2025, Month.JANUARY, 1));

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            var violations = validator.validate(user);
            assertEquals(1, violations.stream()
                    .toList().size(), "Валидация не выполнена");
            assertEquals("PastOrPresent",
                    violations.stream()
                            .toList()
                            .getFirst()
                            .getConstraintDescriptor()
                            .getAnnotation().annotationType().getSimpleName(),
                    "Не работает валидация на некорректную дату рождения пользователя");
        }
    }

    private User buildValidTestUser() {
        Long id = 1L;
        String email = "mail@mail.org";
        String login = "user1";
        String name = "пользователь 1";
        LocalDate birthday = LocalDate.of(1990, Month.MARCH, 1);
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return  user;
    }
}