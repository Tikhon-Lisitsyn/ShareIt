package ru.practicum.item;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item addNew(Long userId, ItemDto itemDto) {
        Item item = toItem(itemDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (item.getAvailable() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Available cannot be null");
        }

        if (item.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Name cannot be null");
        }


        item.setOwner(user);
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item update(Long userId, ItemDto itemDto) {
        Item existingItem = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemDto.getId()));

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not the owner of this item");
        }

        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        return itemRepository.save(existingItem);
    }

    @Override
    public Optional<Item> getOne(Long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public List<Item> getAll(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return itemRepository.findAllByOwner(user);
    }

    @Override
    public List<Item> search(String text) {
        return itemRepository.searchItemByText(text);
    }

    public Comment comment(Long itemId,Long userId, String text) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setText(text);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    private Item toItem(ItemDto itemDto) {
        return modelMapper.map(itemDto,Item.class);
    }
}