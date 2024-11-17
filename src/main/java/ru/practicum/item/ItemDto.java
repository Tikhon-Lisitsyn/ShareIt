package ru.practicum.item;

import lombok.Data;
import ru.practicum.user.User;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private User owner;
    private Boolean available;
}