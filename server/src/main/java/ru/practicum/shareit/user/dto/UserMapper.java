package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserOutputDto toUserOutputDto(User user) {
        UserOutputDto userOutputDto = new UserOutputDto();
        userOutputDto.setId(user.getId());
        userOutputDto.setName(user.getName());
        userOutputDto.setEmail(user.getEmail());
        return userOutputDto;
    }

    public static User toUser(UserInputDto userInputDto) {
        User user = new User();
        user.setName(userInputDto.getName());
        user.setEmail(userInputDto.getEmail());
        return user;
    }
}