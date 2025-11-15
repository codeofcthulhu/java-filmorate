package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    final private FilmService filmService;

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("received an HTTP request to create a film {}", film);
        return filmService.create(film);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("The HTTP request for all films has been accepted and successfully processed");
        return filmService.getAll();
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("received an HTTP request to update a film {}", film);
        return filmService.update(film);
    }
}
