package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long idCounter = 0L;


    @Override
    public List<Item> getAllUserItems(Long userId) {
        return items.values().stream().filter(item -> item.getOwner().equals(userId)).toList();
    }

    @Override
    public Optional<Item> getById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> searchByParam(String text) {
        return items.values().stream()
                .filter(item -> item.getName().toUpperCase().contains(text.toUpperCase()) || item.getDescription().toUpperCase().contains(text.toUpperCase()))
                .filter(Item::getAvailable)
                .toList();
    }

    @Override
    public Item create(Item item) {
        item.setId(++idCounter);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void deleteById(Long id) {
        items.remove(id);
    }
}