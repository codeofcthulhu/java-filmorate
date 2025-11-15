package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private FilmStorage filmStorage;

    public Film create(@Validated(OnCreate.class) Film film) {
        log.info("HTTP film adding request successfully processed {}", film);
        return filmStorage.create(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film update(@Validated(OnUpdate.class) Film film) {
        Long id = film.getId();
        if (filmStorage.findById(id) == null) {
            String errorMessage = String.format("Film with id: %d is not found", id);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        log.info("HTTP film update request successfully processed {}", film);
        return filmStorage.update(film);
    }

}
