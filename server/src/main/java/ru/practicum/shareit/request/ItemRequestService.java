package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ItemRequestOutputDto create(ItemRequestInputDto itemRequestInputDto, Long userId) {
        validateUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestInputDto, userId);
        return ItemRequestMapper.toItemRequestOutputDto(itemRequestRepository.save(itemRequest));
    }

    public List<ItemRequestOutputDto> getAll() {
        return itemRequestRepository.findAllByOrderByCreatedDesc().stream()
                .map(ItemRequestMapper::toItemRequestOutputDto)
                .toList();
    }

    public List<ItemRequestOutputDto> getAllUsersItemRequests(Long userId) {
        validateUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorOrderByCreatedDesc(userId);
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestOutputDto)
                .toList();
    }

    public ItemRequestOutputDto getDetailedItemRequest(Long requestId) {
        ItemRequest itemRequest = validateItemRequest(requestId);
        ItemRequestOutputDto res = ItemRequestMapper.toItemRequestOutputDto(itemRequest);
        List<ItemOutputDto> list = itemRepository.findByRequest(requestId).stream()
                .map(ItemMapper::toItemOutputDto)
                .toList();
        System.out.println(list);
        res.setItems(list);
        return res;
    }

    private void validateUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не существует"));
    }

    private ItemRequest validateItemRequest(Long id) {
        return itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос с id = " + id + " не существует"));
    }
}