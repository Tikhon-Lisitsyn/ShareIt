package ru.practicum.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Item {
    @Positive(message = "ID вещи не может быть отрицательным")
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    private Boolean available;
    @Positive(message = "ID владельца не может быть отрицательным")
    private Long owner;
}