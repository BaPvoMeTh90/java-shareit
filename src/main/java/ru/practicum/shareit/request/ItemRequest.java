package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ItemRequest {
    @Positive
    private Long id;
    @NotBlank
    private String description;
    @Positive
    private Long requestor;
    @NotNull
    private Instant created;
}
