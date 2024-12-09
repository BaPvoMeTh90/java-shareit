package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public List<ItemDto> getAllByUserId(Long userId) {
        return itemRepository.getAllByUserId(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    public ItemDto getById(Long id) {
        //проверка существования вещи
        return ItemMapper.toItemDto(itemRepository.getById(id));
    }

    public List<ItemDto> getAllAvailableByParam(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.getAllAvailableByParam(text).stream().map(ItemMapper::toItemDto).toList();
    }

    public ItemDto create(Long userId, ItemDto itemDto) {
        //проверка существования пользователя
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);
        return ItemMapper.toItemDto(itemRepository.create(item));
    }

    public Item update(Long userId, Long itemId, Item item) {
        //проверка существования вещи
        //проверка существования пользователя
        Item currentItem = itemRepository.getById(itemId);
        //проверка владельца вещи для удаления
        item.setId(itemId);

        if (item.getName() == null || item.getName().isBlank()) {
            item.setName(currentItem.getName());
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            item.setDescription(currentItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(currentItem.getAvailable());
        }
        return itemRepository.update(item);
    }

    public void deleteById(Long userId, Long itemId) {
        //проверка владельца вещи для удаления
        itemRepository.deleteById(itemId);
    }
}