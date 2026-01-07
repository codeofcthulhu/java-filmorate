package ru.yandex.practicum.filmorate.storage.film.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

@Component
public class MpaMapper implements RowMapper<Mpa> {

    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long mpaId = rs.getLong("mpa_id");
        String name = rs.getString("name");
        return Mpa.builder().id(mpaId).name(name).build();
    }
}
