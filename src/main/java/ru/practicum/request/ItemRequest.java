package ru.practicum.request;

import lombok.Data;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Long id;
    private String description;
    private Long requestor;
    private LocalDateTime created;
}