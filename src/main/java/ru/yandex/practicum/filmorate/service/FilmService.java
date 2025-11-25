package ru.yandex.practicum.filmorate.service;

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

    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film create(Film film) {
        log.info("film adding request successfully processed {}", film);
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
        return filmStorage.getMostLikedFilms(count);
    }

    public Film findFilmOrThrow(Long filmId) {
        return EntityFinder.findOrThrow(filmId, filmStorage::findById, "Film");
    }

}
