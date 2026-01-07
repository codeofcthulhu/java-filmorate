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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.film.mapper.FilmResultSetExtractor;
import ru.yandex.practicum.filmorate.storage.user.mapper.UserMapper;

@Repository
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private static final String CREATE_QUERY =
            "INSERT INTO films (name, description, release_date, duration, mpa) " + "VALUES (?, ?, ?, ?, ?);";
    private static final String ADD_FILM_GENRES_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?);";
    private static final String GET_ALL_QUERY = "SELECT f.*, fg.genre_id FROM films AS f LEFT JOIN film_genres AS fg "
            + "ON f.film_id = fg.film_id;";
    private static final String UPDATE_QUERY =
            "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ? " + "WHERE film_id = ?;";
    private static final String DELETE_GENRES_QUERY = "DELETE FROM film_genres WHERE film_id=?;";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?;";
    private static final String GET_GENRES_BY_FILM_ID = "SELECT genre_id FROM film_genres WHERE film_id = ?;";
    private static final String GET_MOST_LIKED_FILMS = "SELECT film_id, COUNT(film_id) AS likes_count FROM likes "
            + "GROUP BY film_id ORDER BY likes_count DESC, film_id LIMIT ?;";
    private static final String ADD_LIKE = "INSERT INTO likes (user_id, film_id) VALUES (?, ?);";
    private static final String GET_LIKES = "SELECT user_id AS l.user_id FROM likes AS l WHERE film_id = ?;";
    private static final String DELETE_LIKE = "DELETE FROM likes WHERE user_id = ? AND film_id = ?;";
    private final JdbcTemplate jdbcTemplate;
    private final FilmResultSetExtractor filmResultSetExtractor;
    private final FilmMapper filmMapper;
    private final UserMapper userMapper;

    @Override
    public Film create(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, film.getMpa());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            film.setId(id);
        } else {
            throw new InternalServerException("Couldn't save data");
        }

        addGenres(film);

        return film;
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, filmResultSetExtractor);
    }

    @Override
    public Film update(Film film) {
        int rowsUpdated = jdbcTemplate.update(UPDATE_QUERY, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa(), film.getId());
        if (rowsUpdated == 0) {
            throw new InternalServerException("Couldn't update data");
        }
        updateGenres(film);
        return film;
    }

    @Override
    public Film findById(Long id) {
        Film film = jdbcTemplate.queryForObject(GET_BY_ID_QUERY, filmMapper, id);
        List<Integer> genres = jdbcTemplate.queryForList(GET_GENRES_BY_FILM_ID, Integer.class);
        film.setGenresIds(genres);
        return film;
    }

    @Override
    public List<Film> getMostLikedFilms(int count) {
        List<Long> mostLikedFilmsIds = jdbcTemplate.queryForList(GET_MOST_LIKED_FILMS, Long.class, count);
        return mostLikedFilmsIds.stream().map(this::findById).toList();
    }

    public List<User> addLike(Long id, Long userId) {
            jdbcTemplate.update(ADD_LIKE, userId, id);
            return getLikes(id);
    }

    @Override
    public List<User> deleteLike(Long id, Long userId) {
        jdbcTemplate.update(DELETE_LIKE, userMapper, userId, id);
        return getLikes(id);
    }

    private List<User> getLikes(Long filmId) {
        return jdbcTemplate.query(GET_LIKES, userMapper, filmId);
    }

    private void addGenres(Film film) {
        List<Integer> filmGenres = film.getGenresIds();
        if (filmGenres == null || filmGenres.isEmpty()) {
            return;
        }

        for (Integer genreId : film.getGenresIds()) {
            jdbcTemplate.update(ADD_FILM_GENRES_QUERY, film.getId(), genreId);
        }
    }

    private void updateGenres(Film film) {
        jdbcTemplate.update(DELETE_GENRES_QUERY, film.getId());
        addGenres(film);
    }
}
