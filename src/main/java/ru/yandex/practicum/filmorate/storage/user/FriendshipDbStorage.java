package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.mapper.UserMapper;

@Repository
@RequiredArgsConstructor
public class FriendshipDbStorage {

    private static final String ADD_FRIEND = """
            INSERT INTO friendships (user_id, friend_id)
            VALUES (?, ?)
            """;
    private static final String GET_FRIENDS = """
            SELECT *
            FROM users AS u
            RIGHT OUTER JOIN friendships AS f ON f.friend_id = u.user_id
            WHERE f.user_id = ?;
            """;
    private static final String DELETE_FRIEND = """
             DELETE
             FROM friendships
             WHERE user_id = ?
               AND friend_id = ?;
            """;
    private static final String GET_COMMON_FRIENDS = """
            SELECT u.*
            FROM friendships AS f1
            JOIN friendships AS f2 ON f1.friend_id = f2.friend_id
            JOIN users AS u ON u.user_id = f1.friend_id
            WHERE f1.user_id = ?
              AND f2.user_id = ?
            """;

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public List<User> getFriends(Long id) {
        return jdbcTemplate.query(GET_FRIENDS, userMapper, id);
    }

    public void addFriend(Long userId, Long friendId) {
        jdbcTemplate.update(ADD_FRIEND, userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        jdbcTemplate.update(DELETE_FRIEND, userId, friendId);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        return jdbcTemplate.query(GET_COMMON_FRIENDS, userMapper, id, otherId);
    }
}
