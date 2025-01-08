package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestInputDto itemRequestInputDto, Long requestor /*User requestor*/) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestInputDto.getDescription());
        itemRequest.setRequestor(requestor);
        return itemRequest;
    }

    public static ItemRequestOutputDto toItemRequestOutputDto(ItemRequest itemRequest) {
        ItemRequestOutputDto itemRequestOutputDto = new ItemRequestOutputDto();
        itemRequestOutputDto.setId(itemRequest.getId());
        itemRequestOutputDto.setDescription(itemRequest.getDescription());
        itemRequestOutputDto.setRequestor(itemRequest.getRequestor()/*UserMapper.toUserOutputDto(itemRequest.getRequestor())*/);
        itemRequestOutputDto.setCreated(itemRequest.getCreated());
        return itemRequestOutputDto;
    }
}