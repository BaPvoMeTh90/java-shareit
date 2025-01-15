package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemInputDto {
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long requestId;
}