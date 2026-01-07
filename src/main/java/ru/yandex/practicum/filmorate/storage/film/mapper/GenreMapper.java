package ru.yandex.practicum.filmorate.storage.film.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

@Component
public class GenreMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long genreId = rs.getLong("genre_id");
        String name = rs.getString("name");
        return Genre.builder().id(genreId).name(name).build();
    }
}
