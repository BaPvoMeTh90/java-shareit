package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ItemRequestServiceTests {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private ItemRequestOutputDto itemRequestOutputDto;
    private Long userId;
    private Long requestId;

    @BeforeEach
    void beforeEach() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user = userRepository.save(user);
        userId = user.getId();

        ItemRequestInputDto itemRequestInputDto = new ItemRequestInputDto();
        itemRequestInputDto.setDescription("Test Request");

        itemRequestOutputDto = itemRequestService.create(itemRequestInputDto, userId);
        requestId = itemRequestOutputDto.getId();
    }

    @Test
    void shouldCreateItemRequest() {
        ItemRequestInputDto newItemRequestInputDto = new ItemRequestInputDto();
        newItemRequestInputDto.setDescription("New Test Request");

        ItemRequestOutputDto newItemRequestOutputDto = itemRequestService.create(newItemRequestInputDto, userId);
        assertNotNull(newItemRequestOutputDto);
        assertEquals(newItemRequestInputDto.getDescription(), newItemRequestOutputDto.getDescription());
    }

    @Test
    void shouldGetAll() {
        List<ItemRequestOutputDto> requests = itemRequestService.getAll();
        assertFalse(requests.isEmpty());
        assertEquals(1, requests.size());
        assertEquals(itemRequestOutputDto.getId(), requests.getFirst().getId());
    }

    @Test
    void shouldGetAllUsersItemRequests() {
        List<ItemRequestOutputDto> requests = itemRequestService.getAllUsersItemRequests(userId);
        assertFalse(requests.isEmpty());
        assertEquals(1, requests.size());
        assertEquals(itemRequestOutputDto.getId(), requests.getFirst().getId());
    }

    @Test
    void shouldGetDetailedItemRequest() {
        ItemRequestOutputDto request = itemRequestService.getDetailedItemRequest(requestId);
        assertNotNull(request);
        assertEquals(itemRequestOutputDto.getId(), request.getId());
        assertEquals(itemRequestOutputDto.getDescription(), request.getDescription());
    }
}
