package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<ItemDto> getAllUserItems(Long userId) {
        return itemRepository.getAllUserItems(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    public ItemDto getById(Long id) {
        validateItem(id);
        return ItemMapper.toItemDto(itemRepository.getById(id));
    }

    public List<ItemDto> searchByParam(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.searchByParam(text).stream().map(ItemMapper::toItemDto).toList();
    }

    public ItemDto create(Long userId, ItemDto itemDto) {
        validateUser(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);
        return ItemMapper.toItemDto(itemRepository.create(item));
    }

    public Item update(Long userId, Long itemId, Item item) {
        validateItem(itemId);
        validateUser(userId);
        Item theItem = itemRepository.getById(itemId);
        validateOwner(userId, theItem);
        item.setId(itemId);

        if (item.getName() == null || item.getName().isBlank()) {
            item.setName(theItem.getName());
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            item.setDescription(theItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(theItem.getAvailable());
        }
        return itemRepository.update(item);
    }

    public void deleteById(Long userId, Long itemId) {
        validateOwner(userId, itemRepository.getById(itemId));
        itemRepository.deleteById(itemId);
    }

    private void validateItem(Long id) {
        if (itemRepository.getById(id) == null) {
            throw new NotFoundException("Item с id = " + id + " не найдена.");
        }
    }

    private void validateUser(Long id) {
        if (userRepository.getById(id) == null) {
            throw new NotFoundException("User с id = " + id + " не найден.");
        }
    }

    private void validateOwner(Long userId, Item item) {
        if (!userId.equals(item.getOwner())) {
            throw new ValidationException("Item с id = " + item.getId() + " не принадлежит пользователю с id = " + userId);
        }
    }
}