package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    public User create(User user);

    public List<User> getAll();

    public User update(User user);

    public User findById(Long id);

    List<User> getFriends(Long id);

    List<User> deleteFriend(Long id, Long friendId);

    List<User> addFriend(Long id, Long friendId);
}
