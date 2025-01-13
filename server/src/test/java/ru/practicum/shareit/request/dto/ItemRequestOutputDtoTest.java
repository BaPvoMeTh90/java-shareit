package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemOutputDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestOutputDtoTest {

    @Autowired
    private final JacksonTester<ItemRequestOutputDto> json;

    @Test
    void testSerialize() throws Exception {

        List<ItemOutputDto> items = new ArrayList<>();

        ItemOutputDto itemOutputDto = new ItemOutputDto();
        itemOutputDto.setId(1L);
        itemOutputDto.setName("test");
        itemOutputDto.setDescription("test");
        itemOutputDto.setAvailable(true);
        itemOutputDto.setOwner(1L);
        itemOutputDto.setRequest(1L);
        itemOutputDto.setLastBooking(null);
        itemOutputDto.setNextBooking(null);
        itemOutputDto.setComments(null);

        items.add(itemOutputDto);

        ItemRequestOutputDto itemRequestOutputDto = new ItemRequestOutputDto();
        itemRequestOutputDto.setId(1L);
        itemRequestOutputDto.setDescription("description");
        itemRequestOutputDto.setRequestor(1L);
        itemRequestOutputDto.setCreated(LocalDateTime.of(2025, 1, 14, 1, 1 ,1));
        itemRequestOutputDto.setItems(items);

        JsonContent<ItemRequestOutputDto> result = json.write(itemRequestOutputDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.requestor");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).hasJsonPath("$.items");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestOutputDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestOutputDto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.requestor").isEqualTo(itemRequestOutputDto.getRequestor().intValue());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(itemRequestOutputDto.getCreated().toString());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(itemOutputDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(itemOutputDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo(itemOutputDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(itemOutputDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].owner").isEqualTo(itemOutputDto.getOwner().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].request").isEqualTo(itemOutputDto.getRequest().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].lastBooking").isNull();
        assertThat(result).extractingJsonPathStringValue("$.items[0].nextBooking").isNull();
        assertThat(result).extractingJsonPathStringValue("$.items[0].comments").isNull();
    }
}
