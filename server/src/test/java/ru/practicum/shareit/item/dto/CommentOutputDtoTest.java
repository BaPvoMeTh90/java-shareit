package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentOutputDtoTest {

    @Autowired
    private final JacksonTester<CommentOutputDto> json;

    @Test
    void testSerialize() throws Exception {

        Item item = new Item();
        item.setId(1L);
        item.setName("test");
        item.setDescription("test");
        item.setAvailable(true);
        item.setOwner(1L);
        item.setRequest(1L);

        CommentOutputDto commentOutputDto = new CommentOutputDto();
        commentOutputDto.setId(1L);
        commentOutputDto.setText("text");
        commentOutputDto.setItem(item);
        commentOutputDto.setAuthorName("authorName");
        commentOutputDto.setCreated(LocalDateTime.of(2025, 1, 14, 1, 1, 1));

        JsonContent<CommentOutputDto> result = json.write(commentOutputDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.text");
        assertThat(result).hasJsonPath("$.item.id");
        assertThat(result).hasJsonPath("$.item.name");
        assertThat(result).hasJsonPath("$.item.description");
        assertThat(result).hasJsonPath("$.item.available");
        assertThat(result).hasJsonPath("$.item.owner");
        assertThat(result).hasJsonPath("$.item.request");
        assertThat(result).hasJsonPath("$.authorName");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(commentOutputDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentOutputDto.getText());
        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo(commentOutputDto.getItem().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .isEqualTo(commentOutputDto.getItem().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo(commentOutputDto.getItem().getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.item.owner")
                .isEqualTo(commentOutputDto.getItem().getOwner().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.item.request")
                .isEqualTo(commentOutputDto.getItem().getRequest().intValue());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available")
                .isEqualTo(commentOutputDto.getItem().getAvailable().booleanValue());
        assertThat(result).extractingJsonPathStringValue("$.authorName")
                .isEqualTo(commentOutputDto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(commentOutputDto.getCreated().truncatedTo(ChronoUnit.MICROS).toString());

    }
}