package ru.yandex.practicum.filmorate.storage.user;

import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.mapper.UserMapper;

@Repository
@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private static final String CREATE_QUERY = """
            INSERT INTO users (name, login, email, birthday)
            VALUES (?, ?, ?, ?);
            """;
    private static final String GET_ALL_QUERY = """
            SELECT * FROM users;
            """;
    private static final String UPDATE_QUERY = """
            UPDATE users
            SET name = ?,
                login = ?,
                email = ?,
                birthday = ?
            WHERE user_id = ?;
            """;
    private static final String GET_BY_ID_QUERY = """
            SELECT *
            FROM users
            WHERE user_id = ?;
            """;
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public User create(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setObject(1, user.getName());
            ps.setObject(2, user.getLogin());
            ps.setObject(3, user.getEmail());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            user.setId(id);
        } else {
            throw new InternalServerException("Couldn't save data");
        }

        return user;
    }

    @Override
    public User findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, userMapper, id);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, userMapper);
    }

    @Override
    public User update(User user) {
        int rowsUpdated = jdbcTemplate.update(UPDATE_QUERY, user.getName(), user.getLogin(), user.getEmail(),
                user.getBirthday(), user.getId());
        if (rowsUpdated == 0) {
            throw new InternalServerException("Couldn't update data");
        }
        return user;
    }
}
