package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.ItemStatus;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTests {

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    BookingRepository bookingRepository;

    @Autowired
    private MockMvc mvc;

    private BookingInputDto bookingInputDto;
    private BookingOutputDto bookingOutputDto;
    private List<BookingOutputDto> bookingOutputDtoList;
    private final Long id = 1L;

    private static final int COUNTER = 10;
    private static final Random RANDOM = new Random();

    @BeforeEach
    void beforeEach() {
        List<ItemOutputDto> itemOutputDtoList = new ArrayList<>();
        for (long i = 1; i <= COUNTER; i++) {
            ItemOutputDto itemToList = new ItemOutputDto();
            itemToList.setId(i);
            itemToList.setName(randomString());
            itemToList.setDescription(randomString());
            itemToList.setAvailable(true);
            itemToList.setOwner(i);
            itemToList.setLastBooking(null);
            itemToList.setNextBooking(null);
            itemToList.setComments(null);
            itemOutputDtoList.add(itemToList);
        }

        ItemOutputDto itemOutputDto = itemOutputDtoList.getFirst();

        Item item = new Item();
        item.setId(itemOutputDto.getId());
        item.setName(itemOutputDto.getName());
        item.setDescription(itemOutputDto.getDescription());
        item.setAvailable(itemOutputDto.getAvailable());
        item.setOwner(itemOutputDto.getOwner());
        item.setRequest(null);

        List<UserOutputDto> userOutputDtoList = new ArrayList<>();
        UserOutputDto userOutputDto;
        for (long i = 1; i <= COUNTER; i++) {
            userOutputDto = new UserOutputDto();
            userOutputDto.setId(i);
            userOutputDto.setName(randomString());
            userOutputDto.setEmail(randomString() + "@gmail.com");
            userOutputDtoList.add(userOutputDto);
        }

        userOutputDto = userOutputDtoList.getFirst();

        User user = new User();
        user.setId(userOutputDto.getId());
        user.setName(userOutputDto.getName());
        user.setEmail(userOutputDto.getEmail());

        bookingOutputDtoList = new ArrayList<>();
        for (long i = 1; i <= COUNTER; i++) {
            bookingOutputDto = new BookingOutputDto();
            bookingOutputDto.setId(i);
            bookingOutputDto.setStart(LocalDateTime.now().plusMinutes(1));
            bookingOutputDto.setEnd(LocalDateTime.now().plusDays(1));
            bookingOutputDto.setItem(itemOutputDto);
            bookingOutputDto.setBooker(userOutputDto);
            bookingOutputDto.setStatus(Status.WAITING);
            bookingOutputDtoList.add(bookingOutputDto);
        }

        bookingOutputDto = bookingOutputDtoList.getFirst();

        Booking booking = new Booking();
        booking.setId(bookingOutputDto.getId());
        booking.setStart(bookingOutputDto.getStart());
        booking.setEnd(bookingOutputDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(bookingOutputDto.getStatus());

        bookingInputDto = new BookingInputDto();
        bookingInputDto.setStart(bookingOutputDto.getStart());
        bookingInputDto.setEnd(bookingOutputDto.getEnd());
        bookingInputDto.setItemId(bookingOutputDto.getItem().getId());
        bookingInputDto.setBookerId(bookingOutputDto.getBooker().getId());
        bookingInputDto.setStatus(bookingOutputDto.getStatus());
    }

    @Test
    void shouldCreateBooking() throws Exception {
        when(bookingService.create(any(), anyLong()))
                .thenReturn(bookingOutputDto);

        mvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", bookingOutputDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingOutputDto.getStart()
                        .truncatedTo(ChronoUnit.MICROS).toString())))
                .andExpect(jsonPath("$.end", is(bookingOutputDto.getEnd()
                        .truncatedTo(ChronoUnit.MICROS).toString())))
                .andExpect(jsonPath("$.item.id", is(bookingOutputDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingOutputDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingOutputDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingOutputDto.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingOutputDto.getStatus().name())));

        verify(bookingService, times(1)).create(eq(bookingInputDto), eq(id));
    }

    @Test
    void shouldUpdateBookingStatus() throws Exception {
        bookingOutputDto.setStatus(Status.APPROVED);

        when(bookingService.updateBookingStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingOutputDto);

        mvc.perform(patch("/bookings/" + bookingOutputDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", bookingOutputDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingOutputDto.getStatus().name())));

        verify(bookingService, times(1)).updateBookingStatus(eq(id), eq(id), eq(true));
    }

    @Test
    void shouldGetBookingById() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenReturn(bookingOutputDto);

        mvc.perform(get("/bookings/" + bookingOutputDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", bookingOutputDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Long.class));

        verify(bookingService, times(1))
                .getById(eq(bookingOutputDto.getId()), eq(bookingOutputDto.getBooker().getId()));
    }

    @Test
    void shouldGetAllUsersBookings() throws Exception {
        when(bookingService.getAllUsersBookings(anyLong(), any()))
                .thenReturn(bookingOutputDtoList);

        mvc.perform(get("/bookings")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", bookingOutputDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(bookingOutputDtoList.size())))
                .andExpect(jsonPath("$[0].id", is(bookingOutputDtoList.getFirst().getId().intValue())));

        verify(bookingService, times(1)).getAllUsersBookings(eq(id), eq(ItemStatus.ALL));
    }

    @Test
    void shouldGetAllOwnersBookings() throws Exception {
        when(bookingService.getAllOwnersBookings(anyLong(), any()))
                .thenReturn(bookingOutputDtoList);

        mvc.perform(get("/bookings/owner")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", bookingOutputDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(bookingOutputDtoList.size())))
                .andExpect(jsonPath("$[0].id", is(bookingOutputDtoList.getFirst().getId().intValue())));

        verify(bookingService, times(1)).getAllOwnersBookings(eq(id), eq(ItemStatus.ALL));
    }

    private String randomString() {
        byte[] array = new byte[12];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
