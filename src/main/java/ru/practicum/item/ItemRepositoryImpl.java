package ru.practicum.item;

import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Repository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Long,Item> items;
    private Map<Long, List<Item>> usersItems;
    private final UserRepositoryImpl userRepository;

    @Override
    public Item getItem(Long id) throws BadRequestException {
        isValidId(id);
        return items.get(id);
    }

    @Override
    public Item addNewItem(Item item) {
        if (item.getOwner() == null) {
            throw new NotFoundException("ID пользователя не передан");
        }

        if (item.getAvailable() == null) {
            throw new ValidationException("Не передан статус вещи");
        }

        if (item.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }

        if (item.getDescription().isBlank()) {
            throw new ValidationException("Описание не может быть пустым");
        }

        if (!userRepository.getUsers().containsKey(item.getOwner())) {
            throw new NotFoundException("Не найден пользователь с ID = " + item.getOwner());
        }

        item.setId(generateId());
        items.put(item.getId(), item);
        List<Item> allItems = usersItems.getOrDefault(item.getOwner(), new ArrayList<>());
        allItems.add(item);
        usersItems.put(item.getOwner(), allItems);
        return item;
    }

    @Override
    public Item updateItem(Item item, Long userId) throws BadRequestException {
        isValidId(item.getId());
        Item updatedItem = items.get(item.getId());
        if (!updatedItem.getOwner().equals(userId)) {
            throw new NotFoundException("Пользователь не имеет прав на изменение этой вещи");
        }

        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }

        return updatedItem;
    }

    @Override
    public List<Item> getUsersItems(Long userId) throws BadRequestException {
        if (!usersItems.containsKey(userId)) {
            throw new BadRequestException("Неверный ID пользователя");
        }
        return usersItems.get(userId);
    }

    @Override
    public List<Item> searchItems(String text) {
        List<Item> searchedItems = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return searchedItems;
        }

        for (Item item : items.values()) {
            if (Boolean.TRUE.equals(item.getAvailable())) {
                if (item.getName() != null && item.getDescription() != null &&
                        (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase()))) {
                    searchedItems.add(item);
                }
            }
        }
        return searchedItems;
    }


    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if (!usersItems.containsKey(userId)) {
            throw new RuntimeException("Не найден пользователь с ID = " + userId);
        }

        List<Item> userItems = usersItems.get(userId);
        Item itemToRemove = null;
        for (Item item : userItems) {
            if (item.getId() == itemId) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove == null) {
            throw new RuntimeException("Не найдена вещь с ID = " + itemId + " у пользователя с ID = " + userId);
        }


        userItems.remove(itemToRemove);
        usersItems.put(userId,userItems);
        items.remove(itemId);
    }

    private void isValidId(Long id) throws BadRequestException {
        if (id == null || !items.containsKey(id)) {
            throw new BadRequestException("Неверный ID вещи");
        }
        if (!usersItems.containsKey(items.get(id).getOwner())) {
            throw new BadRequestException("Неверный ID пользователя");
        }
    }

    private long generateId() {
        long lastId = items.values().stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}