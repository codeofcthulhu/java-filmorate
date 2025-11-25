package ru.yandex.practicum.filmorate.storage.film;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> idToFilm = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public Film create(Film film) {
        film.setId(idCounter++);
        idToFilm.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(idToFilm.values());
    }

    @Override
    public Film update(Film film) {
        idToFilm.put(film.getId(), film);
        return film;
    }

    @Override
    public Film findById(Long id) {
        return idToFilm.get(id);
    }

    public List<Film> getMostLikedFilms(int count) {
        return getAll().stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(film -> film.getLikesByUserId().size())))
                .limit(count).toList();
    }

}
