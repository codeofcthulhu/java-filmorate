package ru.yandex.practicum.filmorate.storage.film;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.user.mapper.UserMapper;

@Repository
@RequiredArgsConstructor
public class LikeDbStorage {
    private static final String ADD_LIKE = """
            INSERT INTO likes (user_id, film_id)
            VALUES (?, ?);
            """;
    private static final String DELETE_LIKE = """
            DELETE
            FROM likes
            WHERE user_id = ?
              AND film_id = ?;
            """;
    private static final String GET_MOST_LIKED_FILMS = """
            SELECT f.*
            FROM films AS f
            LEFT JOIN likes l ON f.film_id = l.film_id
            GROUP BY f.film_id
            ORDER BY COUNT(l.film_id) DESC, f.film_id DESC
            LIMIT ?;
            """;
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    private final FilmMapper filmMapper;

    public void addLike(Long id, Long userId) {
        jdbcTemplate.update(ADD_LIKE, userId, id);
    }

    public void deleteLike(Long id, Long userId) {
        jdbcTemplate.update(DELETE_LIKE, userMapper, userId, id);
    }

    public List<Film> getMostLikedFilmsIds(int count) {
        return jdbcTemplate.query(GET_MOST_LIKED_FILMS, filmMapper, count);
    }
}
