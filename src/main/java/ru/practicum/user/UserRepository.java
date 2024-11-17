package ru.practicum.user;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface UserRepository {

    User getUser(Long userId) throws BadRequestException;

    User addUser(User user);

    User updateUser(User user) throws BadRequestException;

    void removeUser(Long userId) throws BadRequestException;
}