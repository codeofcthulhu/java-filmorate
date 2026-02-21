package ru.yandex.practicum.filmorate.storage.film.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

@Component
public class FilmResultSetExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Film> idToFilm = new HashMap<>();

        while (rs.next()) {
            long currentFilmId = rs.getLong("film_id");

            if (idToFilm.containsKey(currentFilmId)) {
                Film existingFilm = idToFilm.get(currentFilmId);
                existingFilm.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
            } else {
                String name = rs.getString("name");
                String description = rs.getString("description");
                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                Integer duration = rs.getInt("duration");
                Integer mpa = rs.getInt("mpa");
                String mpaName = rs.getString("mpa_name");
                Film newFilm = Film.builder().id(currentFilmId).name(name).description(description)
                        .releaseDate(releaseDate).duration(duration).mpa(new Mpa(mpa, mpaName))
                        .genres(new ArrayList<>(List.of(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")))))
                        .build();
                idToFilm.put(currentFilmId, newFilm);
            }
        }

        return new ArrayList<>(idToFilm.values());
    }


}
