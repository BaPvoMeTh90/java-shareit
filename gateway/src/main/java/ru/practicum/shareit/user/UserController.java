package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.user.dto.UserInputDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        log.info("Поступил запрос на получение всех пользователей.");
        return userClient.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable Long id) {
        log.info("Поступил запрос на получение пользователя с id = {}.",  id);
        return userClient.findById(id);
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@Valid @RequestBody UserInputDto userInputDto) {
        log.info("Поступил запрос на создание пользователя. Body = {}.", userInputDto);
        return userClient.save(userInputDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,
                                    @RequestBody UserInputDto userInputDto) {
        log.info("Поступил запрос на обновление пользователя с id = {}. Body = {}", id, userInputDto);
        return userClient.update(id, userInputDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Поступил запрос на удаление пользователя с id = {}", id);
        userClient.deleteById(id);
    }
}