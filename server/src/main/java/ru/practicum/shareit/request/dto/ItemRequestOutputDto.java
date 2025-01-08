package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemOutputDto;

import java.time.Instant;
import java.util.List;

@Data
public class ItemRequestOutputDto {
    private Long id;
    private String description;
    private Long requestor;
    private Instant created;
    private List<ItemOutputDto> items;
}
