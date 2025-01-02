package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserInputDto {
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
}
