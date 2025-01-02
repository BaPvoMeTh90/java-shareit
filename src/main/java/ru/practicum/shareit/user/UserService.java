package ru.practicum.shareit.user;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataConflict;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserInputDto;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public List<UserOutputDto> findAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserOutputDto).toList();
    }

    public UserOutputDto findById(Long id) {
        var user =  userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользвоателя с id = " + id + " не найден."));
        return UserMapper.toUserOutputDto(user);
    }

    @Transactional
    public UserOutputDto save(UserInputDto userInputDto) {
        User user = UserMapper.toUser(userInputDto);
        return UserMapper.toUserOutputDto(userRepository.save(user));
    }

    @Transactional
    public UserOutputDto update(Long id, UserInputDto userInputDto) {
        validate(id);
        User user = UserMapper.toUser(userInputDto);
        user.setId(id);
        User oldUser = userRepository.findById(id).get();
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(oldUser.getName());
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(oldUser.getEmail());
        }
        return UserMapper.toUserOutputDto(userRepository.save(user));
    }

    @Transactional
    public void deleteById(Long id) {
        validate(id);
        userRepository.deleteById(id);
    }

    private void validate(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("User с id = " + id + " не найден.");
        }
    }
}