package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserInputDto;
import ru.practicum.shareit.user.dto.UserOutputDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserInputDto userInputDto;
    private UserInputDto secondUserInputDto;
    private UserOutputDto userOutputDto;
    private UserOutputDto secondUserOutputDto;
    private Long userId;

    @BeforeEach
    void BeforeEach() {
        userInputDto = new UserInputDto();
        userInputDto.setName("test Name");
        userInputDto.setEmail("test@test.com");

        secondUserInputDto = new UserInputDto();
        secondUserInputDto.setName("test Name2");
        secondUserInputDto.setEmail("test2@test.com");

        userOutputDto = userService.save(userInputDto);
        userId = userOutputDto.getId();

        secondUserOutputDto = userService.save(secondUserInputDto);
    }

    @Test
    void findAll() {
        List<UserOutputDto> users = userService.findAll();
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
        assertEquals(userOutputDto.getId(), users.get(0).getId());
        assertEquals(userOutputDto.getName(), users.get(0).getName());
        assertEquals(userOutputDto.getEmail(), users.get(0).getEmail());
        assertEquals(secondUserOutputDto.getId(), users.get(1).getId());
        assertEquals(secondUserOutputDto.getName(), users.get(1).getName());
        assertEquals(secondUserOutputDto.getEmail(), users.get(1).getEmail());
    }

    @Test
    void findById() {
        UserOutputDto user = userService.findById(userId);
        assertNotNull(user);
        assertEquals(userOutputDto.getId(), user.getId());
        assertEquals(userOutputDto.getName(), user.getName());
        assertEquals(userOutputDto.getEmail(), user.getEmail());
    }

    @Test
    void save() {
        UserInputDto newUserInputDto = new UserInputDto();
        newUserInputDto.setName("New User");
        newUserInputDto.setEmail("save@save.com");

        UserOutputDto newUserOutputDto = userService.save(newUserInputDto);
        assertNotNull(newUserOutputDto);
        assertEquals(newUserInputDto.getName(), newUserOutputDto.getName());
        assertEquals(newUserInputDto.getEmail(), newUserOutputDto.getEmail());
    }

    @Test
    void update() {
        UserInputDto updatedUserInputDto = new UserInputDto();
        updatedUserInputDto.setName("Updated Name");
        updatedUserInputDto.setEmail("updated@updated.com");

        UserOutputDto updatedUserOutputDto = userService.update(userId, updatedUserInputDto);
        assertNotNull(updatedUserOutputDto);
        assertEquals(updatedUserInputDto.getName(), updatedUserOutputDto.getName());
        assertEquals(updatedUserInputDto.getEmail(), updatedUserOutputDto.getEmail());
    }

    @Test
    void deleteById() {
        userService.deleteById(userId);
        assertThrows(NotFoundException.class, () -> userService.findById(userId));
    }
}