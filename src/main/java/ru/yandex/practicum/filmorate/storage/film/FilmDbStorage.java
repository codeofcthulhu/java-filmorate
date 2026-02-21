package ru.yandex.practicum.filmorate.storage.film;

import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.film.mapper.FilmResultSetExtractor;

@Repository
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private static final String CREATE_QUERY = """
            INSERT INTO films (name, description, release_date, duration, mpa)
            VALUES (?, ?, ?, ?, ?);
            """;
    private static final String GET_ALL_QUERY = """
            SELECT f.*,
                   mr.name AS mpa_name,
                   fg.genre_id,
                   g.name AS genre_name
            FROM films AS f
            JOIN mpa_rating AS mr ON f.mpa = mr.mpa_id
            LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id
            LEFT JOIN genres AS g ON fg.genre_id = g.genre_id;
            """;
    private static final String UPDATE_QUERY = """
            UPDATE films
            SET name = ?,
                description = ?,
                release_date = ?,
                duration = ?,
                mpa = ?
            WHERE film_id = ?;
            """;
    private static final String GET_BY_ID_QUERY = """
            SELECT f.*,
                   mr.name AS mpa_name
            FROM films AS f
            JOIN mpa_rating AS mr ON f.mpa = mr.mpa_id
            WHERE film_id = ?;
            """;

    private final JdbcTemplate jdbcTemplate;
    private final FilmResultSetExtractor filmResultSetExtractor;
    private final FilmMapper filmMapper;

    @Override
    public Film create(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            film.setId(id);
        } else {
            throw new InternalServerException("Couldn't save data");
        }

        return film;
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, filmResultSetExtractor);
    }

    @Override
    public Film update(Film film) {
        int rowsUpdated = jdbcTemplate.update(UPDATE_QUERY, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        if (rowsUpdated == 0) {
            throw new InternalServerException("Couldn't update data");
        }
        return findById(film.getId());
    }

    @Override
    public Film findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, filmMapper, id);
    }
}
