package ru.kantser.game.service.validator;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.scene.Scene;
import ru.kantser.game.model.state.GameState;
import ru.kantser.game.model.state.SceneState;
import ru.kantser.game.service.game.GameManager;
import ru.kantser.game.service.game.SaveGameManager;

@AllArgsConstructor
public class ObjValidator {
    private static final Logger log = LoggerFactory.getLogger(ObjValidator.class);
    private final GameManager gameManager;


    public void ensureSceneLoaded(GameState gameState) {
        if (gameState == null) return;
        if (gameState.getSceneState() == null) return;

        SceneState sceneState = gameState.getSceneState();
        if (sceneState.getCurrentScene() != null) return;

        String sceneId = sceneState.getCurrentSceneId();
        if (sceneId == null) return;

        try {
            Scene scene = gameManager.getSceneBuilder().getScene(sceneId);
            sceneState.setCurrentScene(scene);
        } catch (Exception e) {
            log.warn("Error scene loading {}: {}", sceneId, e.getMessage());
        }
    }
}
