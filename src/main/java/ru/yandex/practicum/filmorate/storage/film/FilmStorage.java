package ru.yandex.practicum.filmorate.storage.film;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface FilmStorage {

    public Film create(Film film);

    public List<Film> getAll();

    public Film update(Film film);

    public Film findById(Long id);

    public List<Film> getMostLikedFilms(int count);

    public List<User> addLike(Long id, Long userId);

    public List<User> deleteLike(Long id, Long userId);
}
