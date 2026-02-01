package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    public User create(User user);

    public List<User> getAll();

    public User update(User user);

    public User findById(Long id);
}
