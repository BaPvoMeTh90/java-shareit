package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    public List<ItemDto> getAllUserItems(Long userId) {
        validateUser(userId);
        return itemRepository.getAllUserItems(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    public ItemDto getById(Long id) {
        var item = itemRepository.getById(id).orElseThrow(()-> new NotFoundException("Item отсутствует."));
        return ItemMapper.toItemDto(item);
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
        Item theItem = ItemMapper.toItem(getById(itemId));
        validateOwner(userId, theItem);

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
        validateOwner(userId, ItemMapper.toItem(getById(itemId)));
        itemRepository.deleteById(itemId);
    }

    private void validateItem(Long id) {
        getById(id);
    }

    private void validateUser(Long id) {
        userService.getById(id);
    }

    private void validateOwner(Long userId, Item item) {
        if (!userId.equals(item.getOwner())) {
            throw new ValidationException("Item с id = " + item.getId() + " не принадлежит пользователю с id = " + userId);
        }
    }
}