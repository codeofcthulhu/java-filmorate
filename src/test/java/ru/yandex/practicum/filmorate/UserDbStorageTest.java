package ru.yandex.practicum.filmorate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.config.TestConfig;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({TestConfig.class, UserDbStorage.class, FriendshipDbStorage.class})
class UserDbStorageTest {

    private final UserDbStorage userStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    @Test
    void createUser() {
        User user = User.builder()
                .login("login")
                .email("mail@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User saved = userStorage.create(user);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void getAllUsers() {
        assertThat(userStorage.getAll()).isEmpty();
    }

    @Test
    void updateUser() {
        User user = userStorage.create(User.builder()
                .login("login")
                .email("mail@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());

        user.setName("updated");

        User updated = userStorage.update(user);

        assertThat(updated.getName()).isEqualTo("updated");
    }

    @Test
    void findUserById() {
        User user = userStorage.create(User.builder()
                .login("login")
                .email("mail@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());

        User found = userStorage.findById(user.getId());

        assertThat(found.getId()).isEqualTo(user.getId());
    }

    @Test
    void addFriend() {
        User u1 = userStorage.create(User.builder()
                .login("u1")
                .email("u1@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());

        User u2 = userStorage.create(User.builder()
                .login("u2")
                .email("u2@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());

        friendshipDbStorage.addFriend(u1.getId(), u2.getId());
        assertThat(friendshipDbStorage.getFriends(u1.getId()))
                .hasSize(1);

    }

    @Test
    void deleteFriend() {
        User u1 = userStorage.create(User.builder()
                .login("u1")
                .email("u1@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());

        User u2 = userStorage.create(User.builder()
                .login("u2")
                .email("u2@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());

        friendshipDbStorage.deleteFriend(u1.getId(), u2.getId());
        assertThat(friendshipDbStorage.getFriends(u1.getId()))
                .isEmpty();
    }

    @Test
    void updateNonExistingUserThrows() {
        User user = User.builder()
                .id(999L)
                .login("x")
                .email("x@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertThatThrownBy(() -> userStorage.update(user))
                .isInstanceOf(RuntimeException.class);
    }
}
