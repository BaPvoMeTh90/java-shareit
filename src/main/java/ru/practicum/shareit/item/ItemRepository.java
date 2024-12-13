package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> getAllUserItems(Long userId);

    Item getById(Long id);

    List<Item> searchByParam(String text);

    Item create(Item item);

    Item update(Item item);

    void deleteById(Long id);
}