package ru.yandex.practicum.filmorate.storage.film;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.mapper.GenreMapper;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage {
    private static final String GET_ALL_QUERY = """
            SELECT *
            FROM genres;
            """;
    private static final String GET_BY_ID_QUERY = """
            SELECT *
            FROM genres
            WHERE genre_id = ?;
            """;
    private static final String ADD_FILM_GENRES_QUERY = """
            INSERT INTO film_genres (film_id, genre_id)
            VALUES (?, ?);
            """;
    private static final String DELETE_GENRES_QUERY = """
            DELETE
            FROM film_genres
            WHERE film_id=?;
            """;
    private static final String GET_GENRES_BY_FILM_ID = """
            SELECT fm.genre_id,
                   g.name
            FROM film_genres AS fm
            LEFT OUTER JOIN genres AS g ON fm.genre_id = g.genre_id
            WHERE film_id = ?
            ORDER BY fm.genre_id;
            """;

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    public List<Genre> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, genreMapper);
    }

    public Genre findById(int id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, genreMapper, id);
    }

    public void addFilmGenres(Film film) {
        List<Genre> filmGenres = film.getGenres();

        List<Object[]> batchArgs = new ArrayList<>();
        for (Genre genre : filmGenres) {
            batchArgs.add(new Object[]{film.getId(), genre.getId()});
        }

        jdbcTemplate.batchUpdate(
                ADD_FILM_GENRES_QUERY,
                batchArgs
        );
    }

    public List<Genre> getFilmGenresById(Long filmId) {
        return jdbcTemplate.query(GET_GENRES_BY_FILM_ID, genreMapper, filmId);
    }

    public void updateGenres(Film film) {
        jdbcTemplate.update(DELETE_GENRES_QUERY, film.getId());
        addFilmGenres(film);
    }
}
