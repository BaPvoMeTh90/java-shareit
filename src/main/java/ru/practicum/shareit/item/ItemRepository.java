package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> getAllByUserId(Long userId);

    Item getById(Long id);

    List<Item> getAllAvailableByParam(String text);

    Item create(Item item);

    Item update(Item item);

    void deleteById(Long id);
}