package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.function.Function;

@Slf4j
@UtilityClass
public class EntityFinder {
    public static <T> T findOrThrow(Long id, Function<Long, T> finder, String name) {
        T entity = finder.apply(id);

        if (entity == null) {
            String errorMessage = String.format("%s with id: %d is not found", name, id);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        log.info("{} {} has been found", name, id);
        return entity;
    }
}
