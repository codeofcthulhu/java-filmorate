package ru.yandex.practicum.filmorate.storage.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Data
@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> idToUser = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public User create(User user) {
        user.setId(idCounter++);
        idToUser.put(user.getId(), user);
        log.info("user creation request successfully processed {}", user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(idToUser.values());
    }

    @Override
    public User update(User user) {
        idToUser.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(Long id) {
        return idToUser.get(id);
    }
}
