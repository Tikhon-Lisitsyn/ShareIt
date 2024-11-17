package ru.practicum.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Positive(message = "ID пользователя не может быть отрицательным числом")

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Email must be valid")
    private String email;
}