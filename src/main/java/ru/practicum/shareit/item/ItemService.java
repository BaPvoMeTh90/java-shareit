package ru.practicum.shareit.item;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public List<ItemOutputDto> getAllUserItems(Long userId) {
        validateUser(userId);
        return ItemMapper.toItemOutputDto(itemRepository.findByOwner(userId));
    }

    public ItemOutputDto getById(Long id) {
        var item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item отсутствует."));
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings = bookingRepository.findByItemIdAndStatus(id, Status.APPROVED, sort);
        List<Comment> comments = commentRepository.findByItemId(id);
        return ItemMapper.toDetailedItemOutputDto(item, bookings, comments);
    }

    public List<ItemOutputDto> searchByParam(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return ItemMapper.toItemOutputDto(itemRepository.search(text));
    }

    @Transactional
    public ItemOutputDto create(Long userId, ItemInputDto itemInputDto) {
        validateUser(userId);
        Item item = ItemMapper.toItem(itemInputDto);
        item.setOwner(userId);
        return ItemMapper.toItemOutputDto(itemRepository.save(item));
    }

    @Transactional
    public ItemOutputDto update(Long userId, Long itemId, ItemInputDto itemInputDto) {
        validateItem(itemId);
        validateUser(userId);
        Item theItem = itemRepository.findById(itemId).get();
        validateOwner(userId, theItem);

        if (itemInputDto.getName() != null) {
            theItem.setName(itemInputDto.getName());
        }
        if (itemInputDto.getDescription() != null) {
            theItem.setDescription(itemInputDto.getDescription());
        }
        if (itemInputDto.getAvailable() != null) {
            theItem.setAvailable(itemInputDto.getAvailable());
        }
        return ItemMapper.toItemOutputDto(itemRepository.save(theItem));
    }

    @Transactional
    public void deleteById(Long userId, Long itemId) {
        validateOwner(userId, itemRepository.findById(itemId).get());
        itemRepository.deleteById(itemId);
    }


    @Transactional
    public CommentOutputDto createComment(Long userId, Long itemId, CommentInputDto commentInputDto) {
        var item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item отсутствует."));
        var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User отсутствует."));
        if (bookingRepository.findByBookerIdAndItemIdAndEndIsBefore(userId, itemId, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("Завершенный букинг вещи с id = " + item.getId() +  " пользователем с id = "
                    + userId + " не найден. Комментарий доступен по завершенному букингу.");
        }
        Comment comment = CommentMapper.toComment(commentInputDto);
        comment.setAuthor(user);
        comment.setItem(item);
        return CommentMapper.toCommentOutputDto(commentRepository.save(comment));
    }

    private void validateItem(Long id) {
        getById(id);
    }

    private void validateUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("User отсутствует."));
    }

    private void validateOwner(Long userId, Item item) {
        if (!userId.equals(item.getOwner())) {
            throw new ValidationException("Item с id = " + item.getId() + " не принадлежит пользователю с id = " + userId);
        }
    }
}