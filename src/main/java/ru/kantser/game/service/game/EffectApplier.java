package ru.kantser.game.service.game;

import ru.kantser.game.model.scene.choice.Effect;
import ru.kantser.game.model.state.PlayerState;

/**
 * Интерфейс для применения эффектов (из Choice.effect) к состоянию игрока.
 * Вынесение логики в отдельный класс улучшает тестируемость и соблюдение SRP.
 */
public interface EffectApplier {
    /**
     * Применить эффект к PlayerState. Реализация должна корректно обрабатывать null-эффекты/значения.
     * @param effect эффект из сцены
     * @param playerState состояние игрока
     */
    void apply(Effect effect, PlayerState playerState);
}
