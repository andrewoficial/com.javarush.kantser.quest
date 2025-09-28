package com.javarush.kantser.quest.service.auth;

import com.javarush.kantser.quest.exception.UserAlreadyExistsException;

public interface AuthService {
    boolean authenticate(String username, String password);
    boolean userExists(String username);
    void createUser(String username, String password) throws UserAlreadyExistsException;
}