package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemOutputDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestOutputDto {
    private Long id;
    private String description;
    private Long requestor;
    private LocalDateTime created;
    private List<ItemOutputDto> items;
}
