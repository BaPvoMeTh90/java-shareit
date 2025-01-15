package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UserOutputDto {
    @Positive
    private Long id;
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
}
