package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class Item {
    @Positive
    private Long id;
    @NotBlank(message = "Для создания объекта нужно заполнить поле name")
    private String name;
    @NotBlank(message = "Для создания объекта нужно заполнить поле description")
    private String description;
    @NotNull
    private Boolean available;
    private Long owner;
    private ItemRequest request;
}