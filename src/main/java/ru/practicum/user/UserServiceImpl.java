package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import ru.practicum.exception.ValidationException;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepositoryImpl repository;
    private final ModelMapper modelMapper;

    public User getUser(Long userId) throws BadRequestException {
        return repository.getUser(userId);
    }

    public User addUser(UserDto userDto) {
        User user = toUser(userDto);
        if (user.getEmail() == null || user.getName() == null) {
            throw new ValidationException("Почта или имя пользователя не может быть null");
        }
        return repository.addUser(user);
    }

    public User updateUser(Long userId, UserDto userDto) throws BadRequestException {
        User user = toUser(userDto);
        user.setId(userId);
        return repository.updateUser(user);
    }

    public void removeUser(Long userId) throws BadRequestException {
        repository.removeUser(userId);
    }

    private User toUser(UserDto userDto) {
        return modelMapper.map(userDto,User.class);
    }
}