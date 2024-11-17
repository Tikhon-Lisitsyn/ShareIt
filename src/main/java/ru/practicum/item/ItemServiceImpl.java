package ru.practicum.item;

import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepositoryImpl itemRepository;
    private final ModelMapper modelMapper;

    @Override
    public Item addNew(Long userId, ItemDto itemDto) {
        Item item = toItem(itemDto);
        item.setOwner(userId);
        return itemRepository.addNewItem(item);
    }

    @Override
    public Item update(Long userId, ItemDto itemDto) throws BadRequestException {
        Item item = toItem(itemDto);
        return itemRepository.updateItem(item,userId);
    }

    @Override
    public Item getOne(Long itemId) throws BadRequestException {
        return itemRepository.getItem(itemId);
    }

    @Override
    public List<Item> getAll(Long userId) throws BadRequestException {
        return itemRepository.getUsersItems(userId);
    }

    @Override
    public List<Item> search(String text) {
        return itemRepository.searchItems(text);
    }

    private Item toItem(ItemDto itemDto) {
        return modelMapper.map(itemDto,Item.class);
    }
}