package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreDbStorage;
import ru.yandex.practicum.filmorate.util.EntityFinder;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public List<Genre> getAll() {
        log.info("getAll genres request successfully processed");
        return genreDbStorage.getAll();
    }

    public Genre findGenreOrThrow(Integer genreId) {
        return EntityFinder.findOrThrow(genreId, genreDbStorage::findById, "Genre");
    }

    public void validateFilmGenres(Film film) {
        if (!(film.getGenres() == null || film.getGenres().isEmpty())) {
            Set<Genre> allGenres = new HashSet<>(genreDbStorage.getAll());
            List<Genre> filmGenres = film.getGenres();
            Set<Genre> uniqueFilmGenres = new HashSet<>(filmGenres);
            if (!allGenres.containsAll(filmGenres)) {
                String errorMessage;
                if (Objects.isNull(film.getId())) {
                    errorMessage = String.format("Film %s has unknown genre", film.getName());
                } else {
                    errorMessage = String.format("Film %d has unknown genre", film.getId());
                }
                log.warn(errorMessage);
                throw new NotFoundException(errorMessage);
            } else {
                film.setGenres(new ArrayList<>(uniqueFilmGenres));
            }
        }
    }

    public void addFilmGenres(Film film) {
        if (!(film.getGenres() == null || film.getGenres().isEmpty())) {
            genreDbStorage.addFilmGenres(film);
        }
    }

    public void updateGenres(Film film) {
        genreDbStorage.updateGenres(film);
    }

    public List<Genre> getFilmGenresById(Long filmId) {
        return genreDbStorage.getFilmGenresById(filmId);
    }
}
