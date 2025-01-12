package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemInputDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getAllUserItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> searchByParam(Long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> create(Long userId, ItemInputDto itemInputDto) {
        return post("", userId, itemInputDto);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemInputDto itemInputDto) {
        return patch("/" + itemId, userId, itemInputDto);
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentInputDto commentInputDto) {
        return post("/" + itemId + "/comment", userId, commentInputDto);
    }

    public ResponseEntity<Object> deleteById(Long userId, Long itemId) {
        return delete("/" + itemId, userId);
    }
}