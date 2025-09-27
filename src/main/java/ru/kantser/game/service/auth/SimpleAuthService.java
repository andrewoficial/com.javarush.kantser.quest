package ru.kantser.game.service.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.exception.UserAlreadyExistsException;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleAuthService implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(SimpleAuthService.class);
    private final Map<String, String> users = new ConcurrentHashMap<>();
    
    public SimpleAuthService() {
        log.info("Constructor");
        // Хардкодим тестовых пользователей
        users.put("admin", "admin123");
        users.put("test", "test123");
    }
    
    @Override
    public boolean authenticate(String username, String password) {
        log.info("authenticate");
        return users.containsKey(username) && users.get(username).equals(password);
    }
    
    @Override
    public boolean userExists(String username) {
        log.info("userExists");
        return users.containsKey(username);
    }
    
    @Override
    public void createUser(String username, String password) throws UserAlreadyExistsException {
        log.info("run createUser with username: [{}] via SimpleAuthService in method createUser", username);
        if (users.containsKey(username)) {
            throw new UserAlreadyExistsException("Registration via SimpleAuthService", username, password);
        }
        users.put(username, password);
    }
}