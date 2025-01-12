package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserInputDto;
import ru.practicum.shareit.user.dto.UserOutputDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserInputDto userInputDto ;
    private UserOutputDto userOutputDto;
    private UserOutputDto secondUserOutputDto;
    private final Long userId = 1L;

    @BeforeEach
    public void beforeEach() {
        userInputDto = new UserInputDto();
        userInputDto.setName("test Name");
        userInputDto.setEmail("test@test.com");
        userOutputDto = new UserOutputDto();
        userOutputDto.setId(userId);
        userOutputDto.setName(userInputDto.getName());
        userOutputDto.setEmail(userInputDto.getEmail());
        secondUserOutputDto = new UserOutputDto();
        secondUserOutputDto.setId(2L);
        secondUserOutputDto.setName("test Name2");
        secondUserOutputDto.setEmail("test2@test.com");
    }

    @Test
    void findAllUsers()throws Exception {
        when(userService.findAll()).thenReturn(List.of(userOutputDto, secondUserOutputDto));
        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(userOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.[0].name", is(userOutputDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(userOutputDto.getEmail())))
                .andExpect(jsonPath("$.[1].id", is(secondUserOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.[1].name", is(secondUserOutputDto.getName())))
                .andExpect(jsonPath("$.[1].email", is(secondUserOutputDto.getEmail())));
    }

    @Test
    void findUserById()throws Exception {
        when(userService.findById(anyLong())).thenReturn(userOutputDto);
        mockMvc.perform(get("/users/" + userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(userOutputDto.getName())))
                .andExpect(jsonPath("$.email", is(userOutputDto.getEmail())));
    }

    @Test
    void saveUser() throws Exception {
        when(userService.save(any(UserInputDto.class))).thenReturn(userOutputDto);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(userOutputDto.getName())))
                .andExpect(jsonPath("$.email", is(userOutputDto.getEmail())));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.update(anyLong(), any(UserInputDto.class))).thenReturn(userOutputDto);

        mockMvc.perform(patch("/users/" + userId)
                        .content(objectMapper.writeValueAsString(userInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(userOutputDto.getName())))
                .andExpect(jsonPath("$.email", is(userOutputDto.getEmail())));
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete("/users/" + userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}