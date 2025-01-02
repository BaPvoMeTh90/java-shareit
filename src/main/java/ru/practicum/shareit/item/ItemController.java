package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemOutputDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос пользователя {} на получение всех его вещей", userId);
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/{id}")
    public ItemOutputDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long id) {
        log.info("Поступил запрос пользователя с ID = {}, на получение вещи с id = {}", userId, id);
        return itemService.getById(id);
    }

    @GetMapping("/search")
    public List<ItemOutputDto> searchByParam(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestParam String text) {
        log.info("Поступил запрос пользователя {} на поиск вещи по описанию: {}", userId, text);
        return itemService.searchByParam(text);
    }

    @PostMapping
    public ItemOutputDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody ItemInputDto itemInputDto) {
        log.info("Поступил запрос пользователя {} на добавление вещи. Body:{}", userId, itemInputDto);
        return itemService.create(userId, itemInputDto);
    }

    @PatchMapping("/{itemId}")
    public ItemOutputDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @RequestBody ItemInputDto itemInputDto,
                       @PathVariable Long itemId) {
        log.info("Поступил запрос пользователя {} на изменение Вещи с id = {}. Body {}", userId, itemId, itemInputDto);
        return itemService.update(userId, itemId, itemInputDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long itemId) {
        log.info("Поступил запрос пользователя {} на удаление Вещи с id = {}", userId, itemId);
        itemService.deleteById(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutputDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestBody CommentInputDto commentInputDto,
                                   @PathVariable Long itemId) {
        log.info("Поступил запрос от пользователя с id = {} Коментарий для Вещи с id = {}. Body = {}", userId, itemId, commentInputDto);
        return itemService.createComment(userId, itemId, commentInputDto);
    }
}