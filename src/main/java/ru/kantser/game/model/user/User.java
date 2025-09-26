package ru.kantser.game.model.user;

import java.time.LocalDateTime;

public class User {
    private final String username;
    private final String passwordHash;
    private final LocalDateTime createdAt;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры
    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', createdAt=" + createdAt + "}";
    }
}