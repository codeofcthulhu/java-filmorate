package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.EntityFinder;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final MpaService mpaService;
    private final GenreService genreService;

    public Film create(Film film) {
        mpaService.findMpaOrThrow(film.getMpa().getId());
        film.getGenres().forEach(genre -> genreService.findGenreOrThrow(genre.getId()));
        return filmStorage.create(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film update(Film film) {
        Long id = film.getId();
        findFilmOrThrow(id);
        return filmStorage.update(film);
    }

    public void addLike(Long id, Long userId) {
        findFilmOrThrow(id);
        userService.findUserOrThrow(userId);
        filmStorage.addLike(id, userId);
        log.info("User {} liked film {}", userId, id);
    }

    public void deleteLike(Long id, Long userId) {
        findFilmOrThrow(id);
        userService.findUserOrThrow(userId);
        filmStorage.deleteLike(id, userId);
        log.info("User {} remove like from film {}", userId, id);
    }

    public List<Film> getMostLikedFilms(int count) {
        return filmStorage.getMostLikedFilms(count);
    }

    public Film findFilmOrThrow(Long filmId) {
        return EntityFinder.findOrThrow(filmId, filmStorage::findById, "Film");
    }
}
