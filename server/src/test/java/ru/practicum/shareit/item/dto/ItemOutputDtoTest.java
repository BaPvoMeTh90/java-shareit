package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemOutputDtoTest {

    @Autowired
    private final JacksonTester<ItemOutputDto> json;

    @Test
    void testSerialize() throws Exception {
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

        JsonContent<ItemOutputDto> result = this.json.write(itemOutputDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.owner");
        assertThat(result).hasJsonPath("$.request");
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).hasJsonPath("$.comments");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemOutputDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemOutputDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemOutputDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemOutputDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.owner").isEqualTo(itemOutputDto.getOwner().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.request").isEqualTo(itemOutputDto.getRequest().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking").isNull();
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking").isNull();
        assertThat(result).extractingJsonPathNumberValue("$.comments").isNull();

    }

}