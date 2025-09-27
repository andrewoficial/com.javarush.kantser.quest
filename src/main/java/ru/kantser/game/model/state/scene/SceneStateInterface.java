package ru.kantser.game.model.state.scene;

import ru.kantser.game.model.scene.GameScene;

// Интерфейс для состояния сцены
public interface SceneStateInterface {
    String getCurrentSceneId();
    void setCurrentSceneId(String currentSceneId);
    GameScene getCurrentScene(); // новый метод для получения полного объекта сцены
    void setCurrentScene(GameScene scene); // новый метод для установки полного объекта

    void setTipText(String description);

    String getTipText();
}