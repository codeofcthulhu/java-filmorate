package ru.yandex.practicum.filmorate.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.yandex.practicum.filmorate.storage.film.mapper.*;
import ru.yandex.practicum.filmorate.storage.user.mapper.UserMapper;

@TestConfiguration
public class TestConfig {

    @Bean
    public FilmMapper filmMapper() {
        return new FilmMapper();
    }

    @Bean
    public FilmResultSetExtractor filmResultSetExtractor() {
        return new FilmResultSetExtractor();
    }

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    public GenreMapper genreMapper() {
        return new GenreMapper();
    }

    @Bean
    public MpaMapper mpaMapper() {
        return new MpaMapper();
    }
}