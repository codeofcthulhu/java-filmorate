package ru.yandex.practicum.filmorate.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyLikedException;
import ru.yandex.practicum.filmorate.exception.NoLikeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.EntityFinder;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    final private FilmStorage filmStorage;
    final private UserService userService;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film update(Film film) {
        Long id = film.getId();
        findFilmOrThrow(id);
        log.info("HTTP film update request successfully processed {}", film);
        return filmStorage.update(film);
    }

    public Film addLike(Long id, Long userId) {
        Film film = findFilmOrThrow(id);
        userService.findUserOrThrow(userId);
        if (!film.getLikesByUserId().add(userId)) {
            String errorMessage = String.format("User %d have already liked film %d", userId, id);
            log.warn(errorMessage);
            throw new AlreadyLikedException(errorMessage);
        }
        log.info("User {} liked film {}", userId, id);
        return film;
    }

    public Film deleteLike(Long id, Long userId) {
        Film film = findFilmOrThrow(id);
        userService.findUserOrThrow(userId);
        if (!film.getLikesByUserId().remove(userId)) {
            String errorMessage = String.format("User %d did not like film %d", userId, id);
            log.warn(errorMessage);
            throw new NoLikeException(errorMessage);
        }
        log.info("User {} remove like from film {}", userId, id);
        return film;
    }

    public List<Film> getMostLikedFilms(int count) {
        log.info("request for most liked films successfully proceed");
        return filmStorage.getAll().stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(film -> film.getLikesByUserId().size())))
                .limit(count).toList();
    }

    public Film findFilmOrThrow(Long filmId) {
        return EntityFinder.findOrThrow(filmId, filmStorage::findById, "Film");
    }

}
