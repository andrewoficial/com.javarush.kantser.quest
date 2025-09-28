package ru.kantser.game.service.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.scene.Scene;
import ru.kantser.game.model.state.GameState;
import ru.kantser.game.model.state.PlayerState;
import ru.kantser.game.model.state.SceneState;
import ru.kantser.game.service.files.SceneJsonBuilder;

import java.io.IOException;

public class GameStateValidator {
    private static final Logger log = LoggerFactory.getLogger(GameStateValidator.class);
    private final SceneJsonBuilder sceneBuilder;

    public GameStateValidator(SceneJsonBuilder sceneBuilder) {
        this.sceneBuilder = sceneBuilder;
    }

    public void validateGameState(GameState state) {
        if (state == null) {
            throw new IllegalArgumentException("GameState cannot be null");
        }
        
        validatePlayerState(state.getPlayerState());
        validateSceneState(state.getSceneState());
    }

    public void validatePlayerState(PlayerState playerState) {
        if (playerState == null) {
            throw new IllegalStateException("PlayerState cannot be null");
        }
        
        if (!playerState.isValid()) {
            throw new IllegalStateException("Invalid PlayerState: " + playerState.getPlayerId());
        }
    }

    public void validateSceneState(SceneState sceneState) {
        if (sceneState == null) {
            throw new IllegalStateException("SceneState cannot be null");
        }
        
        if (sceneState.getCurrentSceneId() == null || sceneState.getCurrentSceneId().isBlank()) {
            throw new IllegalStateException("Current scene ID cannot be null or empty");
        }
    }

    public Scene validateAndLoadScene(String sceneId) throws IOException {
        if (sceneId == null || sceneId.isBlank()) {
            throw new IllegalArgumentException("Scene ID cannot be null or empty");
        }
        
        Scene scene = sceneBuilder.getScene(sceneId);
        if (scene == null) {
            throw new IllegalStateException("Scene not found: " + sceneId);
        }
        
        return scene;
    }

    public void ensureSceneLoaded(GameState gameState) {
        if (gameState == null || gameState.getSceneState() == null) return;

        SceneState sceneState = gameState.getSceneState();
        if (sceneState.getCurrentScene() != null) return;

        String sceneId = sceneState.getCurrentSceneId();
        if (sceneId == null) return;

        try {
            Scene scene = sceneBuilder.getScene(sceneId);
            sceneState.setCurrentScene(scene);
        } catch (Exception e) {
            log.warn("Error scene loading {}: {}", sceneId, e.getMessage());
        }
    }
}