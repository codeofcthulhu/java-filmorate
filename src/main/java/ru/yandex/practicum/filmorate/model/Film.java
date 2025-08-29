package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.ValidReleaseDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film {
    private Long id;
    @NotBlank(message = "Film name must not be empty")
    private String name;
    @Size(min = 0, max = 200, message = "The maximum description length is 200 characters")
    private String description;
    @ValidReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "Duration must be positive number")
    private int duration;
}
