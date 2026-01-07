package ru.yandex.practicum.filmorate.util;

import java.util.function.Function;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Slf4j
@UtilityClass
public class EntityFinder {

    public static <T> T findOrThrow(Long id, Function<Long, T> finder, String name) {
        try {
            T entity = finder.apply(id);
            log.info("{} {} has been found", name, id);
            return entity;
        } catch (EmptyResultDataAccessException e) {
            String errorMessage = String.format("%s with id: %d is not found", name, id);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

    }
}
