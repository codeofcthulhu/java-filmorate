package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaDbStorage;
import ru.yandex.practicum.filmorate.util.EntityFinder;

@RequiredArgsConstructor
@Service
@Slf4j
public class MpaService {
    final MpaDbStorage mpaDbStorage;

    public List<Mpa> getAll() {
        log.info("getAll mpa request successfully processed");
        return mpaDbStorage.getAll();
    }

    public Mpa findMpaOrThrow(long mpaId) {
        return EntityFinder.findOrThrow(mpaId, mpaDbStorage::findById, "Mpa");
    }
}