package ru.practicum.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/{userId}")
    public User get(@PathVariable @Valid Long userId) throws BadRequestException {
        return userServiceImpl.getUser(userId);
    }

    @PostMapping
    public User add(@RequestBody @Valid UserDto userDto) {
        return userServiceImpl.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable @Valid Long userId,
                       @RequestBody UserDto userDto) throws BadRequestException {
        return userServiceImpl.updateUser(userId,userDto);
    }

    @DeleteMapping("/{userId}")
    public void remove(@Valid Long userId) throws BadRequestException {
        userServiceImpl.removeUser(userId);
    }
}