package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentInputDto {
    @NotBlank
    private String text;
    private LocalDateTime created;
}