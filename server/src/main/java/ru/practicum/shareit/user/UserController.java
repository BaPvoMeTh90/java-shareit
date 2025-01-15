package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.user.dto.UserInputDto;
import ru.practicum.shareit.user.dto.UserOutputDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserOutputDto> findAllUsers() {
        log.info("Поступил запрос на получение всех пользователей.");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserOutputDto findUserById(@PathVariable Long id) {
        log.info("Поступил запрос на получение пользователя с id = {}.",  id);
        return userService.findById(id);
    }

    @PostMapping
    public UserOutputDto saveUser(@Valid @RequestBody UserInputDto userInputDto) {
        log.info("Поступил запрос на создание пользователя. Body = {}.", userInputDto);
        return userService.save(userInputDto);
    }

    @PatchMapping("/{id}")
    public UserOutputDto updateUser(@PathVariable Long id,
                                    @RequestBody UserInputDto userInputDto) {
        log.info("Поступил запрос на обновление пользователя с id = {}. Body = {}", id, userInputDto);
        return userService.update(id, userInputDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Поступил запрос на удаление пользователя с id = {}", id);
        userService.deleteById(id);
    }
}