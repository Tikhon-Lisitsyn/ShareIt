package ru.practicum.user;

import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Repository;
import ru.practicum.exception.EmailAlreadyExistsException;
import ru.practicum.exception.ValidationException;


import java.util.Map;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private Map<Long,User> users;

    @Override
    public User getUser(Long id) throws BadRequestException {
        isValidId(id);
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Неверный формат почты пользователя");
        }

        for (User useri : users.values()) {
            if (useri.getEmail().equals(user.getEmail())) {
                throw new EmailAlreadyExistsException("Пользователь с такой почтой уже существует");
            }
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) throws BadRequestException {
        isValidId(user.getId());
        User existingUser = users.get(user.getId());

        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())
                && emailAlreadyExists(user.getEmail())) {
            throw new EmailAlreadyExistsException("Пользователь с такой почтой уже существует");
        }

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        users.put(user.getId(), existingUser);
        return existingUser;
    }

    @Override
    public void removeUser(Long id) {

        users.remove(id);
    }

    private long generateId() {
        long lastId = users.values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }

    private void isValidId(Long id) throws BadRequestException {
        if (id == null || !users.containsKey(id)) {
            throw new BadRequestException("Неверный ID пользователя");
        }
    }

    private boolean emailAlreadyExists(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public Map<Long,User> getUsers() {
        return users;
    }
}