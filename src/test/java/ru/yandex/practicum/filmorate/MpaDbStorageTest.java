package ru.yandex.practicum.filmorate;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.config.TestConfig;
import ru.yandex.practicum.filmorate.storage.film.MpaDbStorage;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({TestConfig.class, MpaDbStorage.class})
class MpaDbStorageTest {

    private final MpaDbStorage mpaStorage;

    @Test
    void getAllMpa() {
        assertThat(mpaStorage.getAll()).hasSize(5);
    }

    @Test
    void findMpaMin() {
        assertThat(mpaStorage.findById(1).getId()).isEqualTo(1);
    }

    @Test
    void findMpaMax() {
        assertThat(mpaStorage.findById(5).getId()).isEqualTo(5);
    }
}
