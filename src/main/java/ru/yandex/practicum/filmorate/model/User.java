package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String name;
    @NotBlank(message = "Login must not be empty")
    @Pattern(regexp = "^\\S+$", message = "Login must be a single word without spaces")
    private String login;
    @Email(message = "Email is invalid")
    private String email;
    @PastOrPresent(message = "Birth date can't be in future time")
    private LocalDate birthday;
}
