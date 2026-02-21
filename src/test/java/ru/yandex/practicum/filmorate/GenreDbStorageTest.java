package ru.yandex.practicum.filmorate;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.config.TestConfig;
import ru.yandex.practicum.filmorate.storage.film.GenreDbStorage;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({TestConfig.class, GenreDbStorage.class})
class GenreDbStorageTest {

    private final GenreDbStorage genreStorage;

    @Test
    void getAllGenres() {
        assertThat(genreStorage.getAll()).hasSize(6);
    }

    @Test
    void findGenreByIdMin() {
        assertThat(genreStorage.findById(1).getId()).isEqualTo(1);
    }

    @Test
    void findGenreByIdMax() {
        assertThat(genreStorage.findById(6).getId()).isEqualTo(6);
    }
}
