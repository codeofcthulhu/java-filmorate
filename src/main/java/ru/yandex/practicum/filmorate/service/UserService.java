package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.EntityFinder;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    public User create(User user) {
        checkName(user);
        return userStorage.create(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User update(User user) {
        Long id = user.getId();
        findUserOrThrow(id);
        checkName(user);
        return userStorage.update(user);
    }

    public void addFriend(Long id, Long friendId) {
        findUserOrThrow(id);
        findUserOrThrow(friendId);
        log.info("User {} and user {} successfully become friends", id, friendId);
        friendshipDbStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        User user = findUserOrThrow(id);
        User friend = findUserOrThrow(friendId);
        log.info("user {} and user {} are not friends anymore", id, friendId);
        friendshipDbStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(Long id) {
        User user = findUserOrThrow(id);
        log.info("list of friends of user {} successfully proceed", id);
        return friendshipDbStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        log.info("list of common friends of user {} and user {} successfully proceed", id, otherId);
        return friendshipDbStorage.getCommonFriends(id, otherId);
    }

    public User findUserOrThrow(Long id) {
        return EntityFinder.findOrThrow(id, userStorage::findById, "User");
    }

    private void checkName(User user) {
        String name = user.getName();
        if ((name == null) || (name.isBlank())) {
            user.setName(user.getLogin());
        }
    }
}
