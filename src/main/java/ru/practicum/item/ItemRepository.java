package ru.practicum.item;

import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ItemRepository {

    Item getItem(Long id) throws BadRequestException;

    Item addNewItem(Item item);

    Item updateItem(Item item,Long userId) throws BadRequestException;

    List<Item> getUsersItems(Long userId) throws BadRequestException;

    List<Item> searchItems(String text);
}