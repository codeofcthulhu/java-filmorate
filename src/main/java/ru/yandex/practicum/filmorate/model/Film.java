package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.ValidReleaseDate;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

@Data
@NoArgsConstructor
public class Film {

    @JsonIgnore
    private Set<Long> idsOfLikedUsers = new HashSet<>();
    @NotNull(groups = OnUpdate.class, message = "ID must be specified")
    private Long id;
    @NotBlank(groups = OnCreate.class, message = "Film name must not be empty")
    private String name;
    @Size(groups = {OnCreate.class,
            OnUpdate.class}, min = 0, max = 200, message = "The maximum description length is 200 characters")
    private String description;
    @ValidReleaseDate(groups = {OnCreate.class, OnUpdate.class})
    private LocalDate releaseDate;
    @Positive(groups = {OnCreate.class, OnUpdate.class}, message = "Duration must be positive number")
    private Integer duration;
    private List<Genre> genres = new ArrayList<>();
    private Mpa mpa;

    @Builder
    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, List<Genre> genres, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genres;
        this.mpa = mpa;
    }
}
