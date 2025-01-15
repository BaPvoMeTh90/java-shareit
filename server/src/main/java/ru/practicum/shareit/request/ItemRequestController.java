package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestOutputDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody ItemRequestInputDto itemRequestInputDto) {
        log.info("Поступил запрос пользователя с id = {} на добавление запроса вещи.Body = {}", userId, itemRequestInputDto);
        return itemRequestService.create(itemRequestInputDto, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutputDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос на получение списка запросов вещей от пользователя с id = {}.", userId);
        return itemRequestService.getAll();
    }

    @GetMapping
    public List<ItemRequestOutputDto> getAllUsersItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос пользователя с id = {} на получение списка запросов вместе с данными об ответах на них.", userId);
        return itemRequestService.getAllUsersItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutputDto getDetailedItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @PathVariable Long requestId) {
        log.info("Поступил запрос на получение ItemRequest c id = {}, от пользователя = {}", requestId, userId);
        return itemRequestService.getDetailedItemRequest(requestId);
    }
}
