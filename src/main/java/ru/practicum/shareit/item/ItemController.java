package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос пользователя {} на получение всех его вещей", userId);
        return itemService.getAllByUserId(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable Long id) {
        log.info("Поступил запрос на получение вещи с id = {}", id);
        return itemService.getById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> getAllByParam(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestParam String text) {
        log.info("Поступил запрос пользователя {} на поиск вещи по описанию: {}", userId, text);
        return itemService.getAllAvailableByParam(text);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Validated @RequestBody ItemDto itemDto) {
        log.info("Поступил запрос пользователя {} на добавление вещи. Запрос:{}", userId, itemDto);
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @Validated @RequestBody Item item,
                       @PathVariable Long itemId) {
        log.info("Поступил запрос пользователя {} на изменение Вещи с id = {}. Запрос {}", userId, itemId, item);
        return itemService.update(userId, itemId, item);
    }
}