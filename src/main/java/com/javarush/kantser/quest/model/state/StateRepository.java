package com.javarush.kantser.quest.model.state;

import java.io.IOException;

public interface StateRepository {
    GameState load(String userId, String saveId) throws IOException;

    void save(GameState state, String userId, String saveId) throws IOException;

    boolean exists(String userId, String saveId);
}
