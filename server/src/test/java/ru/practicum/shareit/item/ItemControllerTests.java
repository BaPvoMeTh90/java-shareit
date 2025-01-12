package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
@WebMvcTest(ItemController.class)
public class ItemControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemInputDto itemInputDto;
    private ItemOutputDto itemOutputDto;
    private ItemOutputDto secondItemOutputDto;
    private CommentInputDto commentInputDto;
    private CommentOutputDto commentOutputDto;
    private Long Id = 1L;

    @BeforeEach
    void beforeEach() {
        itemInputDto = new ItemInputDto();
        itemInputDto.setName("Test Item");
        itemInputDto.setDescription("Test Description");
        itemInputDto.setAvailable(true);

        ItemInputDto secondItemInputDto = new ItemInputDto();
        secondItemInputDto.setName("Test2 Item");
        secondItemInputDto.setDescription("Test2 Description");
        secondItemInputDto.setAvailable(true);

        itemOutputDto = new ItemOutputDto();
        itemOutputDto.setId(Id);
        itemOutputDto.setName(itemInputDto.getName());
        itemOutputDto.setDescription(itemInputDto.getDescription());
        itemOutputDto.setAvailable(itemInputDto.getAvailable());

        secondItemOutputDto = new ItemOutputDto();
        secondItemOutputDto.setId(2L);
        secondItemOutputDto.setName(secondItemInputDto.getName());
        secondItemOutputDto.setDescription(secondItemInputDto.getDescription());
        secondItemOutputDto.setAvailable(secondItemInputDto.getAvailable());

        commentInputDto = new CommentInputDto();
        commentInputDto.setText("Test Comment To Item");

        commentOutputDto = new CommentOutputDto();
        commentOutputDto.setId(Id);
        commentOutputDto.setText(commentInputDto.getText());
    }

    @Test
    void shouldGetAllUserItems() throws Exception {
        when(itemService.getAllUserItems(anyLong())).thenReturn(List.of(itemOutputDto, secondItemOutputDto));
        mockMvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", Id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(itemOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.[0].name", is(itemOutputDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemOutputDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemOutputDto.getAvailable())))
                .andExpect(jsonPath("$.[1].id", is(secondItemOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.[1].name", is(secondItemOutputDto.getName())))
                .andExpect(jsonPath("$.[1].description", is(secondItemOutputDto.getDescription())))
                .andExpect(jsonPath("$.[1].available", is(secondItemOutputDto.getAvailable())));;
    }

    @Test
    void shouldGetById() throws Exception {
        when(itemService.getById(anyLong())).thenReturn(itemOutputDto);
        mockMvc.perform(get("/items/" + Id)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", Id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(itemOutputDto.getName())))
                .andExpect(jsonPath("$.description", is(itemOutputDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemOutputDto.getAvailable())));
    }

    @Test
    void shouldSearchByParam() throws Exception {
        when(itemService.searchByParam(anyString())).thenReturn(List.of(secondItemOutputDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "Test2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", Id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(secondItemOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.[0].name", is(secondItemOutputDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(secondItemOutputDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(secondItemOutputDto.getAvailable())));
    }

    @Test
    void shouldCreateItem() throws Exception {
        when(itemService.create(anyLong(), any(ItemInputDto.class))).thenReturn(itemOutputDto);

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", Id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(itemOutputDto.getName())))
                .andExpect(jsonPath("$.description", is(itemOutputDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemOutputDto.getAvailable())));
    }

    @Test
    void shouldUpdateItem()throws Exception {
        when(itemService.update(anyLong(), anyLong(), any(ItemInputDto.class))).thenReturn(itemOutputDto);
        mockMvc.perform(patch("/items/" + Id)
                        .content(objectMapper.writeValueAsString(itemInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", Id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(itemOutputDto.getName())))
                .andExpect(jsonPath("$.description", is(itemOutputDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemOutputDto.getAvailable())));
    }

    @Test
    void shouldDeleteById() throws Exception {
        mockMvc.perform(delete("/items/" + Id)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", Id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateComment() throws Exception {
        when(itemService.createComment(anyLong(), anyLong(), any(CommentInputDto.class))).thenReturn(commentOutputDto);
        mockMvc.perform(post("/items/" + Id + "/comment")
                        .content(objectMapper.writeValueAsString(commentInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", Id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(commentOutputDto.getId().intValue())))
                .andExpect(jsonPath("$.text", is(commentOutputDto.getText())));
    }
}
