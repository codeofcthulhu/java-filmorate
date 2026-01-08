package ru.yandex.practicum.filmorate.storage.film;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.mapper.GenreMapper;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage {
    private static final String GET_ALL_QUERY = "SELECT * FROM genres;";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?;";
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    public List<Genre> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, genreMapper);
    }

    public Genre findById(int id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, genreMapper, id);
    }
}
