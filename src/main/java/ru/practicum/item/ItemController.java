package ru.practicum.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceImpl itemService;
    private final UserRepository userRepository;

    @GetMapping("/{itemId}")
    public Optional<ItemDto> get(@PathVariable Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getOne(itemId, userId);
    }

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") @Valid Long userId,
                    @RequestBody ItemDto itemDto) {
        return itemService.addNew(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") @Valid Long userId,
                       @PathVariable Long itemId,
                       @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        itemDto.setOwner(user);

        return itemService.update(userId, itemDto);
    }

    @GetMapping
    public List<Item> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) throws BadRequestException {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) throws BadRequestException {

        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto comment(@PathVariable @Valid Long itemId,
                              @RequestHeader("X-Sharer-User-Id") @Valid Long userId,
                              @RequestBody CommentRequestDto commentRequest) {
        return itemService.comment(itemId, userId, commentRequest.getText());
    }

}