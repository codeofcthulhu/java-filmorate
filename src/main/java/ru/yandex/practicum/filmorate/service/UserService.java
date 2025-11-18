package ru.yandex.practicum.filmorate.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyNotFriendsException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.EntityFinder;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

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
        log.info("HTTP user update request successfully processed {}", user);
        return userStorage.update(user);
    }

    public User addFriend(Long id, Long friendId) {
        User user = findUserOrThrow(id);
        User friend = findUserOrThrow(friendId);
        if (!(user.getFriendsById().add(friendId)) || !(friend.getFriendsById().add(id))) {
            String errorMessage = String.format("Users with id: %d and %d - are already friends", id, friendId);
            log.warn(errorMessage);
            throw new DuplicatedDataException(errorMessage);
        }
        log.info("User {} and user {} successfully become friends", id, friendId);
        return user;
    }

    public User deleteFriend(Long id, Long friendId) {
        User user = findUserOrThrow(id);
        User friend = findUserOrThrow(friendId);
        if (! (user.getFriendsById().remove(friendId)) || ! (friend.getFriendsById().remove(id))) {
            String errorMessage = String.format("Users with id: %d and %d - are not friends", id, friendId);
            log.warn(errorMessage);
            throw new AlreadyNotFriendsException(errorMessage);
        }
        log.info("user {} and user {} are not friends anymore", id, friendId);
        return user;
    }

    public List<User> getFriends(Long id) {
        User user = findUserOrThrow(id);
        log.info("list of friends of user {} successfully proceed", id);
        return user.getFriendsById().stream().map(userStorage::findById).toList();
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = findUserOrThrow(id);
        User otherUser = findUserOrThrow(otherId);
        log.info("list of common friends of user {} and user {} successfully proceed", id, otherId);
        return user.getFriendsById().stream().filter(otherUser.getFriendsById()::contains).map(userStorage::findById)
                .collect(Collectors.toList());
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
