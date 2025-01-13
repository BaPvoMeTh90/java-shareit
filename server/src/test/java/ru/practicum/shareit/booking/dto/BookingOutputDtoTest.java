package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.user.dto.UserOutputDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingOutputDtoTest {

    @Autowired
    private final JacksonTester<BookingOutputDto> json;

    @Test
    void testSerialize() throws Exception {
        UserOutputDto userOutputDto = new UserOutputDto();
        userOutputDto.setId(1L);
        userOutputDto.setName("John Wick");
        userOutputDto.setEmail("john.wick@comiccon.com");

        ItemOutputDto itemOutputDto = new ItemOutputDto();
        itemOutputDto.setId(1L);
        itemOutputDto.setName("test");
        itemOutputDto.setDescription("test");
        itemOutputDto.setAvailable(true);
        itemOutputDto.setOwner(2L);
        itemOutputDto.setRequest(1L);
        itemOutputDto.setLastBooking(null);
        itemOutputDto.setNextBooking(null);
        itemOutputDto.setComments(null);

        BookingOutputDto bookingOutputDto = new BookingOutputDto();
        bookingOutputDto.setId(1L);
        bookingOutputDto.setStart(LocalDateTime.of(2025, 1, 14, 0, 26 ,20, 000000));
        bookingOutputDto.setEnd(LocalDateTime.of(2025, 1, 14, 0, 26 ,20, 000000).plusHours(1));
        bookingOutputDto.setItem(itemOutputDto);
        bookingOutputDto.setBooker(userOutputDto);
        bookingOutputDto.setStatus(Status.APPROVED);


        JsonContent<BookingOutputDto> result = json.write(bookingOutputDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.item.id");
        assertThat(result).hasJsonPath("$.item.name");
        assertThat(result).hasJsonPath("$.item.description");
        assertThat(result).hasJsonPath("$.item.available");
        assertThat(result).hasJsonPath("$.item.owner");
        assertThat(result).hasJsonPath("$.item.request");
        assertThat(result).hasJsonPath("$.item.lastBooking");
        assertThat(result).hasJsonPath("$.item.nextBooking");
        assertThat(result).hasJsonPath("$.item.comments");
        assertThat(result).hasJsonPath("$.booker.id");
        assertThat(result).hasJsonPath("$.booker.name");
        assertThat(result).hasJsonPath("$.booker.email");
        assertThat(result).hasJsonPath("$.status");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingOutputDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingOutputDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingOutputDto.getEnd().toString());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(itemOutputDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(itemOutputDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(itemOutputDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(itemOutputDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.item.owner").isEqualTo(itemOutputDto.getOwner().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.item.request").isEqualTo(itemOutputDto.getRequest().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.item.lastBooking").isEqualTo(itemOutputDto.getLastBooking());
        assertThat(result).extractingJsonPathNumberValue("$.item.nextBooking").isEqualTo(itemOutputDto.getNextBooking());
        assertThat(result).extractingJsonPathNumberValue("$.item.comments").isEqualTo(itemOutputDto.getComments());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(userOutputDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(userOutputDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo(userOutputDto.getEmail());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(bookingOutputDto.getStatus().toString());

    }

}