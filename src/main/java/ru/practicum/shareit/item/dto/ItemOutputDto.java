package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Data
public class ItemOutputDto {
    @Positive
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private Long owner;
    private ItemRequest request;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<Comment> comments;
}