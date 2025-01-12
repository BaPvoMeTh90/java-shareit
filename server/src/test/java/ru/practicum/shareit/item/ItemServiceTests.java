package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ItemServiceTests {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private ItemInputDto itemInputDto;
    private ItemOutputDto itemOutputDto;
    private CommentInputDto commentInputDto;
    private Long userId;
    private Long itemId;
    private Long itemRequestId;

    @BeforeEach
    void beforeEach() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user = userRepository.save(user);
        userId = user.getId();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Test Request");
        itemRequest.setRequestor(user.getId());
        itemRequest = itemRequestRepository.save(itemRequest);
        itemRequestId = itemRequest.getId();

        itemInputDto = new ItemInputDto();
        itemInputDto.setName("Test Item");
        itemInputDto.setDescription("Text");
        itemInputDto.setAvailable(true);
        itemInputDto.setRequestId(itemRequestId);

        itemOutputDto = itemService.create(userId, itemInputDto);
        itemId = itemOutputDto.getId();

        commentInputDto = new CommentInputDto();
        commentInputDto.setText("Test Comment");

        Booking booking = new Booking();
        booking.setItem(itemRepository.findById(itemId).orElseThrow());
        booking.setBooker(user);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(Status.APPROVED);
        bookingRepository.save(booking);
    }

    @Test
    void shouldGetAllUserItems() {
        List<ItemOutputDto> items = itemService.getAllUserItems(userId);
        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
        assertEquals(itemOutputDto.getId(), items.get(0).getId());
    }

    @Test
    void shouldGetById() {
        ItemOutputDto item = itemService.getById(itemId);
        assertNotNull(item);
        assertEquals(itemOutputDto.getId(), item.getId());
        assertEquals(itemOutputDto.getName(), item.getName());
        assertEquals(itemOutputDto.getDescription(), item.getDescription());
        assertEquals(itemOutputDto.getAvailable(), item.getAvailable());
    }

    @Test
    void shouldSearchByParam() {
        List<ItemOutputDto> items = itemService.searchByParam("Text");
        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
        assertEquals(itemOutputDto.getId(), items.get(0).getId());
    }

    @Test
    void shouldCreateItem() {
        ItemOutputDto itemOutputDto = itemService.create(userId, itemInputDto);
        assertNotNull(itemOutputDto);
        assertEquals(itemInputDto.getName(), itemOutputDto.getName());
        assertEquals(itemInputDto.getDescription(), itemOutputDto.getDescription());
        assertEquals(itemInputDto.getAvailable(), itemOutputDto.getAvailable());
    }

    @Test
    void shouldUpdateItem() {
        ItemInputDto updatedItemInputDto = new ItemInputDto();
        updatedItemInputDto.setName("Updated Name");
        updatedItemInputDto.setDescription("Updated Description");
        updatedItemInputDto.setAvailable(false);
        ItemOutputDto updatedItemOutputDto = itemService.update(userId, itemId, updatedItemInputDto);
        assertNotNull(updatedItemOutputDto);
        assertEquals(updatedItemInputDto.getName(), updatedItemOutputDto.getName());
        assertEquals(updatedItemInputDto.getDescription(), updatedItemOutputDto.getDescription());
        assertEquals(updatedItemInputDto.getAvailable(), updatedItemOutputDto.getAvailable());
    }

    @Test
    void shouldDeleteById() {
        itemService.deleteById(userId, itemId);
        assertThrows(NotFoundException.class, () -> itemService.getById(itemId));
    }

    @Test
    void shouldCreateComment() {
        CommentOutputDto comment = itemService.createComment(userId, itemId, commentInputDto);
        assertNotNull(comment);
        assertEquals(commentInputDto.getText(), comment.getText());
    }
}
