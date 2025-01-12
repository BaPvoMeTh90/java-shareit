package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestInputDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestInputDto itemRequestInputDto;
    private ItemRequestOutputDto itemRequestOutputDto;

    @BeforeEach
    void beforeEach() {
        itemRequestInputDto = new ItemRequestInputDto();
        itemRequestInputDto.setDescription("Test Request");

        itemRequestOutputDto = new ItemRequestOutputDto();
        itemRequestOutputDto.setId(1L);
        itemRequestOutputDto.setDescription(itemRequestInputDto.getDescription());
    }

    @Test
    void shouldCreateItemRequest() throws Exception {
        when(itemRequestService.create(any(ItemRequestInputDto.class), anyLong())).thenReturn(itemRequestOutputDto);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemRequestOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.description", is(itemRequestOutputDto.getDescription())));
    }

    @Test
    void shouldGetAll() throws Exception {
        when(itemRequestService.getAll()).thenReturn(List.of(itemRequestOutputDto));

        mockMvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemRequestOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.[0].description", is(itemRequestOutputDto.getDescription())));
    }

    @Test
    void shouldGetAllUsersItemRequests() throws Exception {
        when(itemRequestService.getAllUsersItemRequests(anyLong())).thenReturn(List.of(itemRequestOutputDto));

        mockMvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemRequestOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.[0].description", is(itemRequestOutputDto.getDescription())));
    }

    @Test
    void shouldGetDetailedItemRequest() throws Exception {
        when(itemRequestService.getDetailedItemRequest(anyLong())).thenReturn(itemRequestOutputDto);

        mockMvc.perform(get("/requests/" + 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemRequestOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.description", is(itemRequestOutputDto.getDescription())));
    }
}
