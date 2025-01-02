package ru.practicum.shareit.item;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Comment> comments = commentRepository.findAll();
          List<ItemOutputDto> items = ItemMapper.toItemOutputDto(itemRepository.findByOwner(userId));
        for (ItemOutputDto item : items) {
            item.setComments(comments.stream()
                    .filter(comment -> comment.getItem().getId().equals(item.getId()))
                    .sorted((c1, c2) -> c2.getCreated().compareTo(c1.getCreated()))
                    .collect(Collectors.toList()));
        }
        return items;
    }

    public ItemOutputDto getById(Long id) {
        ItemOutputDto output = ItemMapper.toItemOutputDto(itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item отсутствует.")));
        output.setComments(commentRepository.findByItemId(id).stream()
                .sorted((c1, c2) -> c2.getCreated().compareTo(c1.getCreated()))
                .toList());
        return output;
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
        var item = validateItem(itemId);
        var user = validateUser(userId);
        if (bookingRepository.findByBookerIdAndItemIdAndEndIsBefore(userId, itemId, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("Завершенный букинг вещи с id = " + item.getId() +  " пользователем с id = "
                    + userId + " не найден. Комментарий доступен по завершенному букингу.");
        }
        Comment comment = CommentMapper.toComment(commentInputDto);
        comment.setAuthor(user);
        comment.setItem(item);
        return CommentMapper.toCommentOutputDto(commentRepository.save(comment));
    }

    private Item validateItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item отсутствует."));
    }

    private User validateUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User отсутствует."));
    }

    private void validateOwner(Long userId, Item item) {
        if (!userId.equals(item.getOwner())) {
            throw new ValidationException("Item с id = " + item.getId() + " не принадлежит пользователю с id = " + userId);
        }
    }
}