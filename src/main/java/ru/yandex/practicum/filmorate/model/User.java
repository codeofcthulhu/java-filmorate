package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @NotNull(groups = OnUpdate.class, message = "ID must be specified")
    private Long id;
    private String name;
    @NotBlank(groups = OnCreate.class, message = "Login must not be empty")
    @Pattern(groups = {OnCreate.class,
            OnUpdate.class}, regexp = "^\\S+$", message = "Login must be a single word without spaces")
    private String login;
    @NotBlank(groups = OnCreate.class, message = "Email must not be blank")
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "Email is invalid")
    private String email;
    @PastOrPresent(groups = {OnCreate.class, OnUpdate.class}, message = "Birth date can't be in future time")
    private LocalDate birthday;
}
