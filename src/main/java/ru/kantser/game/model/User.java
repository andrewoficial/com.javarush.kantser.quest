package ru.kantser.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class User {
    private final String username;
    private final String passwordHash;
    private final LocalDateTime createdAt;

    @Override
    public String toString() {
        return "User{username='" + username + "', createdAt=" + createdAt + "}";
    }
}