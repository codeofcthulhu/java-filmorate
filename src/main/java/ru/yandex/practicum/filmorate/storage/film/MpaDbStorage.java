package ru.yandex.practicum.filmorate.storage.film;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.mapper.MpaMapper;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage {

    private static final String GET_ALL_QUERY = """
            SELECT *
            FROM mpa_rating;
            """;
    private static final String GET_BY_ID_QUERY = """
            SELECT *
            FROM mpa_rating
            WHERE mpa_id = ?;
            """;
    private static final String GET_MPA_BY_FILM_ID = """
            SELECT m.mpa_id,
                   m.name
            FROM mpa_rating AS m
            RIGHT OUTER JOIN films AS f ON m.mpa_id = f.mpa
            WHERE film_id = ?
            """;
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    public List<Mpa> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, mpaMapper);
    }

    public Mpa findById(int id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, mpaMapper, id);
    }

    public Mpa getFilmMpaById(Long filmId) {
        return jdbcTemplate.queryForObject(GET_MPA_BY_FILM_ID, mpaMapper, filmId);
    }
}
