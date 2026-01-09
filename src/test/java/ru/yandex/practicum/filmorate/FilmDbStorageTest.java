package ru.yandex.practicum.filmorate;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.config.TestConfig;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({TestConfig.class, FilmDbStorage.class})
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    @Test
    void createFilmMinimal() {
        Film film = Film.builder()
                .name("film")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(1)
                .mpa(Mpa.builder().id(1).build())
                .build();

        Film saved = filmStorage.create(film);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void createFilmWithGenres() {
        Film film = Film.builder()
                .name("film")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(1000)
                .genres(List.of(new Genre(1, null), new Genre(2, null)))
                .mpa(Mpa.builder().id(5).build())
                .build();

        Film saved = filmStorage.create(film);

        Film found = filmStorage.findById(saved.getId());

        assertThat(found.getGenres()).hasSize(2);
    }

    @Test
    void updateFilm() {
        Film film = filmStorage.create(Film.builder()
                .name("film")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(Mpa.builder().id(1).build())
                .build());

        film.setName("updated");

        Film updated = filmStorage.update(film);

        assertThat(updated.getName()).isEqualTo("updated");
    }

    @Test
    void getAllFilms() {
        assertThat(filmStorage.getAll()).isEmpty();
    }

    @Test
    void getMostLikedFilmsEmpty() {
        assertThat(filmStorage.getMostLikedFilms(10)).isEmpty();
    }
}
