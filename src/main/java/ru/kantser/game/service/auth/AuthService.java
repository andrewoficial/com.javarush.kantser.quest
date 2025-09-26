package ru.kantser.game.service.auth;

import ru.kantser.game.exception.UserAlreadyExistsException;

public interface AuthService {
    boolean authenticate(String username, String password);
    boolean userExists(String username);
    void createUser(String username, String password) throws UserAlreadyExistsException;
}