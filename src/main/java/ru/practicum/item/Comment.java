package ru.practicum.item;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @JoinColumn(name = "author_id")
    @ManyToOne
    private User author;
    private LocalDateTime created;
}