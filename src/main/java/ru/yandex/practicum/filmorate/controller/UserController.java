package ru.yandex.practicum.filmorate.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Long, User> idToUser = new HashMap<>();
    private Long idCounter = 1L;

    @PostMapping
    public User create(@RequestBody @Validated(OnCreate.class) User user) {
        log.info("received an HTTP request to create a user {}", user);
        user.setId(idCounter++);
        checkName(user);
        idToUser.put(user.getId(), user);
        log.info("HTTP user creation request successfully processed {}", user);
        return user;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("The HTTP request for all users has been accepted and successfully processed");
        return new ArrayList<>(idToUser.values());
    }

    @PutMapping
    public User update(@RequestBody @Validated(OnUpdate.class) User user) {
        log.info("received an HTTP request to update a user {}", user);
        Long id = user.getId();
        if (! idToUser.containsKey(id)) {
            String errorMessage = String.format("User with id: %d is not found", id);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        checkName(user);
        idToUser.put(id, user);
        log.info("HTTP user update request successfully processed {}", user);
        return user;
    }

    private void checkName(User user) {
        String name = user.getName();
        if ((name == null) || (name.isBlank())) {
            user.setName(user.getLogin());
        }
    }
}
