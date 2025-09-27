package ru.kantser.game.model.state.scene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.scene.GameScene;
import ru.kantser.game.model.scene.Scene;
import ru.kantser.game.model.state.player.PlayerState;

public class SceneState implements SceneStateInterface {
    private static final Logger log = LoggerFactory.getLogger(SceneState.class);
    private String currentSceneId;
    private transient GameScene currentScene; // transient - не сериализуется в JSON
    private transient String tipText = ""; //лень делать много проверок на нул

    public SceneState() {
        log.info("Создаю SceneState без параметров");
    }

    @JsonCreator
    public SceneState(@JsonProperty("currentSceneId") String currentSceneId) {
        log.info("Создаю SceneState с параметрами");
        this.currentSceneId = currentSceneId;
    }

    @Override
    public String getCurrentSceneId() {
        return currentSceneId;
    }

    @Override
    public void setCurrentSceneId(String currentSceneId) {
        if (currentSceneId == null || currentSceneId.trim().isEmpty()) {
            throw new IllegalArgumentException("Scene ID cannot be null or empty");
        }
        this.currentSceneId = currentSceneId.trim();
        this.currentScene = null; // сбрасываем при смене ID
    }

    @Override
    @JsonIgnore
    public GameScene getCurrentScene() {
        return currentScene;
    }

    @Override
    @JsonIgnore
    public void setTipText(String text){
        tipText = text;
    }

    @Override
    @JsonIgnore
    public String getTipText(){
        return tipText;
    }

    @Override
    public void setCurrentScene(GameScene scene) {
        this.currentScene = scene;
        if (scene != null) {
            this.currentSceneId = scene.getId();
        }
    }

    @Override
    public String toString() {
        return "SceneState{currentSceneId='" + currentSceneId + "', currentScene=" + currentScene + "}";
    }
}