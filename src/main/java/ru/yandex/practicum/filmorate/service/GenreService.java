package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
}
