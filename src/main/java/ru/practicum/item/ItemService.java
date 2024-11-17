package ru.practicum.item;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {

    Item addNew(Long userId, ItemDto itemDto);
    Item update(Long userId, ItemDto itemDto) throws BadRequestException;
    Item getOne(Long itemId) throws BadRequestException;
    List<Item> getAll(Long userId) throws BadRequestException;
    List<Item> search(String text) throws BadRequestException;
}