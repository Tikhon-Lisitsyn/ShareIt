package ru.practicum.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Почта не может быть пустой")
    private String email;
}