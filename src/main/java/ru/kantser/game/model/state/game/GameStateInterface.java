package ru.kantser.game.model.state.game;

import ru.kantser.game.model.state.player.PlayerState;
import ru.kantser.game.model.state.scene.SceneState;

import java.util.Deque;

/**
 * Интерфейс для управления состоянием игры
 */
public interface GameStateInterface {


    PlayerState getPlayerState();

    void setPlayerState(PlayerState playerState);

    SceneState getSceneState();

    void setSceneState(SceneState sceneState);

    Deque<String> getLastSceneIds();

    void setLastSceneIds(Deque<String> lastSceneIds);

}