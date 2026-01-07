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

    private static final String GET_ALL_QUERY = "SELECT * FROM mpa;";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM mpa WHERE mpa_id = ?;";
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    public List<Mpa> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, mpaMapper);
    }

    public Mpa findById(long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, mpaMapper, id);
    }
}
