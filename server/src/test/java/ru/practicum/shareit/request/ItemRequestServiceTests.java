package ru.practicum.shareit.request;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.user.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ItemRequestServiceTests {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private ItemRequestInputDto itemRequestInputDto;
    private ItemRequestOutputDto itemRequestOutputDto;
    private Long userId;
    private Long requestId;
}
