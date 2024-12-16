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
        var user =  userRepository.getById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return UserMapper.toUserDto(user);
    }

    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.create(user));
    }

    public UserDto update(Long id, UserDto userDto) {
        validate(id);
        User user = UserMapper.toUser(userDto);
        user.setId(id);
        User oldUser = UserMapper.toUser(getById(id));
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(oldUser.getName());
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(oldUser.getEmail());
        }
        return UserMapper.toUserDto(userRepository.update(user));
    }

    public void deleteById(Long id) {
        validate(id);
        userRepository.deleteById(id);
    }

    private void validate(Long id) {
        if (userRepository.getById(id).isEmpty()) {
            throw new NotFoundException("User с id = " + id + " не найден.");
        }
    }
}