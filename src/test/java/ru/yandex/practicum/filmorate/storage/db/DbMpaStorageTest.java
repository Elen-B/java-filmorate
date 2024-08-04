package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.mappers.MpaRowMapper;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({DbMpaStorage.class, MpaRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("DbMpaStorage")
class DbMpaStorageTest {
    private final DbMpaStorage mpaStorage;

    @Test
    void shouldReturnFiveMpa() {
        Collection<Mpa> mpaList = mpaStorage.getAll();
        assertEquals(getAllTestMpa().size(), mpaList.size(), "Неверное количество записей рейтинга");
        assertEquals(getAllTestMpa(), mpaList, "Не совпадают наборы записей рейтинга");
    }

    @Test
    void shouldReturnPG13Mpa() {
        Long mpaId = 3L;
        Mpa dbMpa = mpaStorage.getById(mpaId);
        assertEquals("PG-13", dbMpa.getName(), String.format("Неверное название рейтинга для ид = %d", mpaId));
    }

    private static Collection<Mpa> getAllTestMpa() {
        return List.of(
                getMpa(1L, "G"),
                getMpa(2L, "PG"),
                getMpa(3L, "PG-13"),
                getMpa(4L, "R"),
                getMpa(5L, "NC-17")
        );
    }

    private static Mpa getMpa(Long id, String name) {
        Mpa mpa = new Mpa();
        mpa.setId(id);
        mpa.setName(name);
        return mpa;
    }
}