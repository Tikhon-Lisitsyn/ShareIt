package ru.practicum.user;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
interface UserService {
    Optional<User> getUser(Long userId) throws BadRequestException;
    User addUser(UserDto userDto);
    User updateUser(Long userId, UserDto userDto) throws BadRequestException;
    void removeUser(Long userId) throws BadRequestException;
}