package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import ru.practicum.shareit.exception.DataConflict;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emailCheck = new HashSet<>();
    private Long idCounter = 0L;

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public User create(User user) {
        String email = user.getEmail();
        if (emailCheck.contains(email)) {
            throw new DataConflict("email = " + user.getEmail() + " уже используется.");
        }
        user.setId(++idCounter);
        users.put(user.getId(), user);
        emailCheck.add(email);
        return user;
    }

    @Override
    public User update(User user) {
        User theUser = users.get(user.getId());
        theUser.setName(user.getName());
        String newEmail = user.getEmail();
        String oldEmail = theUser.getEmail();
        if (!newEmail.equals(oldEmail) && emailCheck.contains(newEmail)) {
            throw new DataConflict("email = " + user.getEmail() + " уже используется.");
        }
        theUser.setEmail(newEmail);
        emailCheck.add(newEmail);
        emailCheck.remove(oldEmail);
        users.put(user.getId(), theUser);
        return theUser;
    }

    @Override
    public void deleteById(Long id) {
        emailCheck.remove(users.get(id).getEmail());
        users.remove(id);
    }
}