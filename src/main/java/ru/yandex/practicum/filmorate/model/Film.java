package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.ValidReleaseDate;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film {
    @NotNull(groups = OnUpdate.class, message = "ID must be specified")
    private Long id;
    @NotBlank(groups = OnCreate.class, message = "Film name must not be empty")
    private String name;
    @Size(groups = {OnCreate.class, OnUpdate.class}, min = 0, max = 200, message = "The maximum description length is 200 characters")
    private String description;
    @ValidReleaseDate(groups = {OnCreate.class, OnUpdate.class})
    private LocalDate releaseDate;
    @Positive(groups = {OnCreate.class, OnUpdate.class}, message = "Duration must be positive number")
    private int duration;
}
