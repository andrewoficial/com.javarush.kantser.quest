package ru.kantser.game.model.state;

import ru.kantser.game.model.state.game.GameState;

import java.io.IOException;
import java.util.Optional;

public interface UserStateRepository {
    /**
     * Загружает GameState для userId.
     * @param userId идентификатор пользователя
     * @return Optional.empty() если нет сохранения
     */
    Optional<GameState> load(String userId) throws IOException;

    /**
     * Сохраняет GameState для userId (перезаписывает).
     */
    void save(String userId, GameState state) throws IOException;

    /**
     * Проверяет, существует ли сохранение для userId
     */
    boolean exists(String userId);
}
