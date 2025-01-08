package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemOutputDto toItemOutputDto(Item item) {
        ItemOutputDto itemOutputDto = new ItemOutputDto();
        itemOutputDto.setId(item.getId());
        itemOutputDto.setName(item.getName());
        itemOutputDto.setDescription(item.getDescription());
        itemOutputDto.setAvailable(item.getAvailable());
        itemOutputDto.setOwner(item.getOwner());
        itemOutputDto.setRequest(item.getRequest());
        return itemOutputDto;
    }

    public static List<ItemOutputDto> toItemOutputDto(Iterable<Item> items) {
        List<ItemOutputDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(toItemOutputDto(item));
        }
        return result;
    }

    public static Item toItem(ItemInputDto itemInputDto) {
        Item item = new Item();
        item.setName(itemInputDto.getName());
        item.setDescription(itemInputDto.getDescription());
        item.setAvailable(itemInputDto.getAvailable());
        item.setOwner(itemInputDto.getOwner());
        item.setRequest(itemInputDto.getRequestId());
        return item;
    }
}