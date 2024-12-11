package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getAll() {
        return userRepository.getAll().stream().map(UserMapper::toUserDto).toList();
    }

    public UserDto getById(Long id) {
        Validate(id);
        return UserMapper.toUserDto(userRepository.getById(id));
    }

    public User create(User user) {
        return userRepository.create(user);
    }

    public User update(Long id, User user) {
        Validate(id);
        user.setId(id);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(userRepository.getById(id).getName());
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(userRepository.getById(id).getEmail());
        }
        return userRepository.update(user);
    }

    public void deleteById(Long id) {
        Validate(id);
        userRepository.deleteById(id);
    }

    private void Validate(Long id) {
        if (userRepository.getById(id) == null) {
            throw new NotFoundException("User с id = " + id + " не найден.");
        }
    }
}