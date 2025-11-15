package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@RequestBody @Validated(OnCreate.class) User user) {
        log.info("received an HTTP request to create a user {}", user);
        return userService.create(user);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("the HTTP request for all users has been accepted and successfully processed");
        return userService.getAll();
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("received an HTTP request to update a user {}", user);
        return userService.update(user);
    }
}
