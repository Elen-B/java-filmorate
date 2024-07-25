package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Collection<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }

    public Mpa getMpaById(Long id) {
        Mpa mpa = mpaStorage.getById(id);
        if (mpa == null) {
            throw new NotFoundException(String.format("Рейтинг с ид %s не найден", id));
        }
        return mpa;
    }
}
