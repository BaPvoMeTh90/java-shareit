package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentInputDto {
    private String text;
    private LocalDateTime created;
}