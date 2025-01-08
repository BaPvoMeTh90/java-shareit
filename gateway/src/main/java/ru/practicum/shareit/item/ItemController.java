package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemInputDto;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {


    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос пользователя {} на получение всех его вещей", userId);
        return itemClient.getAllUserItems(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long id) {
        log.info("Поступил запрос пользователя с ID = {}, на получение вещи с id = {}", userId, id);
        return itemClient.getById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByParam(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam String text) {
        log.info("Поступил запрос пользователя {} на поиск вещи по описанию: {}", userId, text);
        return itemClient.searchByParam(userId, text);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @Valid @RequestBody ItemInputDto itemInputDto) {
        log.info("Поступил запрос пользователя {} на добавление вещи. Body:{}", userId, itemInputDto);
        return itemClient.create(userId, itemInputDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @RequestBody ItemInputDto itemInputDto,
                                @PathVariable Long itemId) {
        log.info("Поступил запрос пользователя {} на изменение Вещи с id = {}. Body {}", userId, itemId, itemInputDto);
        return itemClient.update(userId, itemId, itemInputDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long itemId) {
        log.info("Поступил запрос пользователя {} на удаление Вещи с id = {}", userId, itemId);
        itemClient.deleteById(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestBody CommentInputDto commentInputDto,
                                   @PathVariable Long itemId) {
        log.info("Поступил запрос от пользователя с id = {} Коментарий для Вещи с id = {}. Body = {}", userId, itemId, commentInputDto);
        return itemClient.createComment(userId, itemId, commentInputDto);
    }
}