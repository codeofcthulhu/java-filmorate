package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@SpringBootTest
class FilmorateApplicationTests {

    private static Validator validator;
    private FilmController filmController;
    private UserController userController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        userController = new UserController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
	void contextLoads() {
	}

    @Test
    void testAddAndGetAllFilmsOneFilmShouldReturn() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("Mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(Duration.ofMinutes(148));

        filmController.add(film);

        List<Film> films = filmController.getAll();
        assertEquals(1, films.size());
        assertEquals("Inception", films.get(0).getName());
    }

    @Test
    void testUpdateFilm() {
        Film film = new Film();
        film.setName("Matrix");
        film.setDescription("Sci-fi classic");
        film.setReleaseDate(LocalDate.of(1999, 3, 31));
        film.setDuration(Duration.ofMinutes(136));

        Film created = filmController.add(film);
        created.setName("Matrix Reloaded");

        Film updated = filmController.update(created);

        assertEquals("Matrix Reloaded", updated.getName());
    }

    @Test
    void testAddingAndGettingSeveralFilms() {
        Film film1 = new Film(
                null,
                "The Shawshank Redemption",
                "A story of hope and friendship in prison",
                LocalDate.of(1994, 9, 23),
                Duration.ofMinutes(142)
        );

        Film film2 = new Film(
                null,
                "Fight Club",
                "An insomniac office worker and a soap maker form an underground fight club",
                LocalDate.of(1999, 10, 15),
                Duration.ofMinutes(139)
        );

        Film film3 = new Film(
                null,
                "Forrest Gump",
                "The life journey of a simple man with a big heart",
                LocalDate.of(1994, 7, 6),
                Duration.ofMinutes(142)
        );

        Film film1FromServer = filmController.add(film1);
        Film film2FromServer = filmController.add(film2);
        Film film3FromServer = filmController.add(film3);

        List<Film> films = filmController.getAll();
        assertEquals(3, films.size());
        List<String> filmsNames = films.stream().map(Film::getName).toList();
        assertTrue(filmsNames.contains("The Shawshank Redemption"));
        assertTrue(filmsNames.contains("Fight Club"));
        assertTrue(filmsNames.contains("Forrest Gump"));
        assertEquals(1, film1FromServer.getId());
        assertEquals(2, film2FromServer.getId());
        assertEquals(3, film3FromServer.getId());
    }

    @Test
    void updateFilmWithNonExistentIdThrowsException() {
        Film film = new Film(
                999L,
                "Ghost Film",
                "This film does not exist in the server",
                LocalDate.of(2020, 1, 1),
                Duration.ofMinutes(120)
        );

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmController.update(film)
        );

        assertEquals("Film with id: 999 is not found", exception.getMessage());
    }

    @Test
    void addFilmWithEmptyNameShouldFailValidation() {
        Film film = new Film(
                null,
                "",
                "Description is fine",
                LocalDate.of(2000, 1, 1),
                Duration.ofMinutes(120)
        );

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testDescriptionMaxLengthValidation() {
        Film film = new Film(
                null,
                "Inception",
                "A".repeat(250),
                LocalDate.of(2010, 7, 16),
                Duration.ofMinutes(148)
        );

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void testReleaseDateValidationTooEarly() {
        Film film = new Film(
                null,
                "Inception",
                "A valid description",
                LocalDate.of(1800, 1, 1),
                Duration.ofMinutes(148)
        );

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("releaseDate")));
    }

    @Test
    void testDurationValidationNegative() {
        Film film = new Film(
                null,
                "Inception",
                "A valid description",
                LocalDate.of(2010, 7, 16),
                Duration.ofMinutes(-120)
        );

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
    }

    @Test
    void addCompletelyEmptyFilmShouldFailValidation() {
        Film film = new Film();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testAddAndGetAllUsersOneUserShouldReturn() {
        User user = new User(null, "Alice", "aliceLogin", "alice@example.com", LocalDate.of(1990, 1, 1));

        userController.create(user);

        List<User> users = userController.getAll();
        assertEquals(1, users.size());
        assertEquals("aliceLogin", users.get(0).getLogin());
    }

    @Test
    void testUpdateUser() {
        User user = new User(null, "Bob", "bobLogin", "bob@example.com", LocalDate.of(1985, 5, 20));
        User created = userController.create(user);
        created.setName("Bobby");

        User updated = userController.update(created);

        assertEquals("Bobby", updated.getName());
    }

    @Test
    void testAddingAndGettingSeveralUsers() {
        User user1 = new User(null, "Alice", "aliceLogin", "alice@example.com", LocalDate.of(1990, 1, 1));
        User user2 = new User(null, "Bob", "bobLogin", "bob@example.com", LocalDate.of(1985, 5, 20));
        User user3 = new User(null, "Charlie", "charlieLogin", "charlie@example.com", LocalDate.of(2000, 7, 15));
        User user1FromServer = userController.create(user1);
        User user2FromServer = userController.create(user2);
        User user3FromServer = userController.create(user3);

        List<User> users = userController.getAll();

        assertEquals(3, users.size());
        List<String> logins = users.stream().map(User::getLogin).toList();
        assertTrue(logins.contains("aliceLogin"));
        assertTrue(logins.contains("bobLogin"));
        assertTrue(logins.contains("charlieLogin"));
        assertEquals(1, user1FromServer.getId());
        assertEquals(2, user2FromServer.getId());
        assertEquals(3, user3FromServer.getId());
    }

    @Test
    void updateUserWithNonExistentIdThrowsException() {
        User user = new User(999L, "Ghost", "ghostLogin", "ghost@example.com", LocalDate.of(1999, 9, 9));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userController.update(user));

        assertEquals("User with id: 999 is not found", exception.getMessage());
    }

    @Test
    void addUserWithEmptyLoginShouldFailValidation() {
        User user = new User(null, "Alice", "", "alice@example.com", LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }

    @Test
    void addUserWithLoginContainingSpacesShouldFailValidation() {
        User user = new User(null, "Alice", "alice login", "alice@example.com", LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }

    @Test
    void addUserWithInvalidEmailShouldFailValidation() {
        User user = new User(null, "Alice", "aliceLogin", "not-an-email", LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void addUserWithFutureBirthdateShouldFailValidation() {
        User user = new User(null, "Alice", "aliceLogin", "alice@example.com", LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthdate")));
    }

    @Test
    void addValidUserPassesValidation() {
        User user = new User(null, "Alice", "aliceLogin", "alice@example.com", LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    void addCompletelyEmptyUserShouldFailValidation() {
        User user = new User();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }
}



