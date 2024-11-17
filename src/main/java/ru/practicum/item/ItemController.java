package ru.practicum.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public Item get(@PathVariable Long itemId) throws BadRequestException {
        return itemService.getOne(itemId);
    }

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") @Valid Long userId,
                    @RequestBody ItemDto itemDto) {
        return itemService.addNew(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") @Valid  Long userId,
                       @PathVariable Long itemId,
                       @RequestBody ItemDto itemDto) throws BadRequestException {
        itemDto.setId(itemId);
        itemDto.setOwner(userId);
        return itemService.update(userId,itemDto);
    }

    @GetMapping
    public List<Item> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) throws BadRequestException {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) throws BadRequestException {

        return itemService.search(text);
    }
}