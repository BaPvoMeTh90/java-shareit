package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getAllUserItems(Long userId);

    Optional<Item> getById(Long id);

    List<Item> searchByParam(String text);

    Item create(Item item);

    Item update(Item item);

    void deleteById(Long id);
}