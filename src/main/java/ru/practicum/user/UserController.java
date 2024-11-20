package ru.practicum.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/{userId}")
    public Optional<User> get(@PathVariable @Valid Long userId) throws BadRequestException {
        return userServiceImpl.getUser(userId);
    }

    @PostMapping
    public User add(@RequestBody @Valid UserDto userDto) {
        return userServiceImpl.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable @Valid Long userId,
                       @RequestBody UserDto userDto) {
        return userServiceImpl.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable @Valid Long userId) {
        userServiceImpl.removeUser(userId);
    }
}