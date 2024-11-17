package ru.practicum.user;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
interface UserService {
    User getUser(Long userId) throws BadRequestException;
    User addUser(UserDto userDto);
    User updateUser(Long userId, UserDto userDto) throws BadRequestException;
    void removeUser(Long userId) throws BadRequestException;
}