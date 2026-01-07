package ru.yandex.practicum.filmorate.storage.film.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Integer duration = rs.getInt("duration");
        Integer mpa = rs.getInt("mpa");
        return Film.builder().id(filmId).name(name).description(description).releaseDate(releaseDate).duration(duration)
                .mpa(mpa).build();
    }
}