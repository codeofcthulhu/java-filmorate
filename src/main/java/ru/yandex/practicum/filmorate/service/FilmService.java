package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.EntityFinder;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film create(Film film) {
        log.info("film adding request successfully processed {}", film);
        return filmStorage.create(film);
    }

    public List<Film> getAll() {
        log.info("getAll request successfully processed");
        return filmStorage.getAll();
    }

    public Film update(Film film) {
        Long id = film.getId();
        findFilmOrThrow(id);
        log.info("HTTP film update request successfully processed {}", film);
        return filmStorage.update(film);
    }

    public List<User> addLike(Long id, Long userId) {
        findFilmOrThrow(id);
        userService.findUserOrThrow(userId);
        List<User> users = filmStorage.addLike(id, userId);
        log.info("User {} liked film {}", userId, id);
        return users;
    }

    public List<User> deleteLike(Long id, Long userId) {
        Film film = findFilmOrThrow(id);
        userService.findUserOrThrow(userId);
        List<User> users = filmStorage.deleteLike(id, userId);
        log.info("User {} remove like from film {}", userId, id);
        return users;
    }

    public List<Film> getMostLikedFilms(int count) {
        log.info("request for most liked films successfully proceed");
        return filmStorage.getMostLikedFilms(count);
    }

    public Film findFilmOrThrow(Long filmId) {
        return EntityFinder.findOrThrow(filmId, filmStorage::findById, "Film");
    }
}
