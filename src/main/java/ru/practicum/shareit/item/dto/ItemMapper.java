package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

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

    public static ItemOutputDto toDetailedItemOutputDto(Item item, List<Booking> bookings, List<Comment> comments) {
        ItemOutputDto itemOutputDto = new ItemOutputDto();
        itemOutputDto.setId(item.getId());
        itemOutputDto.setName(item.getName());
        itemOutputDto.setDescription(item.getDescription());
        itemOutputDto.setAvailable(item.getAvailable());
        itemOutputDto.setOwner(item.getOwner());
        itemOutputDto.setRequest(item.getRequest());
        itemOutputDto.setComments(comments);
        return itemOutputDto;
    }

    public static Item toItem(ItemInputDto itemInputDto) {
        Item item = new Item();
        item.setName(itemInputDto.getName());
        item.setDescription(itemInputDto.getDescription());
        item.setAvailable(itemInputDto.getAvailable());
        item.setOwner(itemInputDto.getOwner());
        item.setRequest(itemInputDto.getRequest());
        return item;
    }
}