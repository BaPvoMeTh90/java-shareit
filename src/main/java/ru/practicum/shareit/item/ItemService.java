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
        validateUser(userId);
        return itemRepository.getAllUserItems(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    public ItemDto getById(Long id) {
        try {
            return ItemMapper.toItemDto(itemRepository.getById(id));
        } catch (NotFoundException e) {
            throw new ValidationException("Item not found");
        }
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

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        validateItem(itemId);
        validateUser(userId);
        Item theItem = itemRepository.getById(itemId);
        validateOwner(userId, theItem);
        itemDto.setId(itemId);

        if (itemDto.getName() != null) {
            theItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            theItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            theItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.update(theItem));
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