package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemRequestInputDto itemRequestInputDto) {
        log.info("Поступил запрос пользователя с id = {} на добавление запроса вещи.Body = {}", userId, itemRequestInputDto);
        return itemRequestClient.create(itemRequestInputDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsersItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос пользователя с id = {} на получение списка своих запросов вместе с данными об ответах на них.", userId);
        return itemRequestClient.getAllUsersItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос на получение списка запросов вещей");
        return itemRequestClient.getAll(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getDetailedById(@PathVariable Long requestId,
                                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос на получение ItemRequest c id = {}", requestId);
        return itemRequestClient.getOneDetailedById(requestId, userId);
    }
}
