package ru.kantser.game.model.state;

import java.io.IOException;

public interface GameDataParser<T> {
    T parseFromJson(String json) throws IOException;
    String convertToJson(T object) throws IOException;
}