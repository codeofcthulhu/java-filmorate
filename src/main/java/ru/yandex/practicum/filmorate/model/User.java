package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @JsonIgnore
    @Builder.Default
    private Set<Long> friendsIds = new HashSet<>();
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
