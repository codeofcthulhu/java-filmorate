package ru.yandex.practicum.filmorate.storage.film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Data
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

}
