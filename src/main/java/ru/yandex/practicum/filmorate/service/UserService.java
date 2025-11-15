package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User create(@Validated(OnCreate.class) User user) {
        checkName(user);
        log.info("HTTP user creation request successfully processed {}", user);
        return userStorage.create(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User update (@Validated(OnUpdate.class) User user) {
        Long id = user.getId();
        if (userStorage.findById(id) == null) {
            String errorMessage = String.format("User with id: %d is not found", id);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        checkName(user);
        log.info("HTTP user update request successfully processed {}", user);
        return userStorage.update(user);
    }

    private void checkName(User user) {
        String name = user.getName();
        if ((name == null) || (name.isBlank())) {
            user.setName(user.getLogin());
        }
    }
}
