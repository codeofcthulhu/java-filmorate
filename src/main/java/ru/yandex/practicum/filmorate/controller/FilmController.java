package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Map<Long, Film> idToFilm = new HashMap<>();
    private Long idCounter = 1L;
    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        log.info("received an HTTP request to add a film {}", film);
        film.setId(idCounter++);
        idToFilm.put(film.getId(), film);
        log.info("HTTP film adding request successfully processed {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("The HTTP request for all films has been accepted and successfully processed");
        return new ArrayList<>(idToFilm.values());
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("received an HTTP request to update a film {}", film);
        Long id = film.getId();
        if (! idToFilm.containsKey(id)) {
            String errorMessage = String.format("Film with id: %d is not found", id);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        idToFilm.put(id, film);
        log.info("HTTP film update request successfully processed {}", film);
        return film;
    }
}
