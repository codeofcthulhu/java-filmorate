package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeDbStorage;
import ru.yandex.practicum.filmorate.util.EntityFinder;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final LikeDbStorage likeDbStorage;
    private final UserService userService;
    private final MpaService mpaService;
    private final GenreService genreService;

    public Film create(Film film) {
        mpaService.findMpaOrThrow(film.getMpa().getId());
        genreService.validateFilmGenres(film);
        filmStorage.create(film);
        genreService.addFilmGenres(film);
        log.info("Film {} successfully added", film.getId());
        return findFilmOrThrow(film.getId());
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film update(Film film) {
        Long id = film.getId();
        findFilmOrThrow(id);
        mpaService.findMpaOrThrow(film.getMpa().getId());
        if (!(film.getGenres() == null || film.getGenres().isEmpty())) {
            genreService.validateFilmGenres(film);
            genreService.updateGenres(film);
        }
        return filmStorage.update(film);
    }

    public void addLike(Long id, Long userId) {
        findFilmOrThrow(id);
        userService.findUserOrThrow(userId);
        likeDbStorage.addLike(id, userId);
        log.info("User {} liked film {}", userId, id);
    }

    public void deleteLike(Long id, Long userId) {
        findFilmOrThrow(id);
        userService.findUserOrThrow(userId);
        likeDbStorage.deleteLike(id, userId);
        log.info("User {} remove like from film {}", userId, id);
    }

    public List<Film> getMostLikedFilms(int count) {
        return likeDbStorage.getMostLikedFilmsIds(count);
    }

    public Film findFilmOrThrow(Long filmId) {
        Film film = EntityFinder.findOrThrow(filmId, filmStorage::findById, "Film");
        film.setGenres(genreService.getFilmGenresById(filmId));
        return film;
    }
}
