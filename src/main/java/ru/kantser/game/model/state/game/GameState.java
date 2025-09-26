package ru.kantser.game.model.state.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.state.player.PlayerState;
import ru.kantser.game.model.state.scene.SceneState;
import ru.kantser.game.service.game.GameManager;

import java.util.ArrayDeque;
import java.util.Deque;

public class GameState implements GameStateInterface {
    private static final Logger log = LoggerFactory.getLogger(GameState.class);
    private PlayerState playerState;
    private SceneState sceneState;
    // Храним два предыдущих состояния сцен (id предыдущих сцен) — как пример
    private Deque<String> lastSceneIds = new ArrayDeque<>(2);

    public GameState() {
        log.info("Создаю GameState без параметров");
    }


    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public SceneState getSceneState() {
        return sceneState;
    }

    public void setSceneState(SceneState sceneState) {
        // при смене сцены — сдвигаем lastSceneIds
        if (this.sceneState != null && this.sceneState.getCurrentSceneId() != null) {
            if (lastSceneIds.size() == 2) {
                lastSceneIds.removeLast();
            }
            lastSceneIds.addFirst(this.sceneState.getCurrentSceneId());
        }
        this.sceneState = sceneState;
    }

    public Deque<String> getLastSceneIds() {
        return lastSceneIds;
    }

    public void setLastSceneIds(Deque<String> lastSceneIds) {
        this.lastSceneIds = lastSceneIds;
    }



    @Override
    public String toString() {
        return "GameState{" +
                ", playerState=" + playerState +
                ", sceneState=" + sceneState +
                ", lastSceneIds=" + lastSceneIds +
                '}';
    }
}