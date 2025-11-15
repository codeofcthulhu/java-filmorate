package ru.yandex.practicum.filmorate.storage.film;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    public Film create(Film film);
    public List<Film> getAll();
    public Film update(Film film);
    public Film findById(Long id);
}
