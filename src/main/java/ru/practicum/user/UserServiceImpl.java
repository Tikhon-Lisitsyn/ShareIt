package ru.practicum.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.ValidationException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;

    public Optional<User> getUser(Long userId) throws BadRequestException {
        return repository.findById(userId);
    }

    public User addUser(UserDto userDto) {
        User user = toUser(userDto);
        if (user.getEmail() == null || user.getName() == null) {
            throw new ValidationException("Почта или имя пользователя не может быть null");
        }

        if (repository.existsByEmail(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }

        return repository.save(user);
    }

    @Transactional
    public User updateUser(Long userId, UserDto userDto) {
        User existingUser = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            if (!existingUser.getEmail().equals(userDto.getEmail()) &&
                    repository.existsByEmail(userDto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
            }
            existingUser.setEmail(userDto.getEmail());
        }

        return repository.save(existingUser);
    }

    public void removeUser(Long userId) {
        repository.deleteById(userId);
    }

    private User toUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}