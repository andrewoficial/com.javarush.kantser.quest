package ru.kantser.game.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kantser.game.model.scene.GameScene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SceneService {
    private final Path scenesDir;
    private final ObjectMapper mapper = new ObjectMapper();

    public SceneService(String scenesPath) {
        this.scenesDir = Path.of(scenesPath);
    }

    public GameScene getScene(String sceneId) throws IOException {
        Path file = scenesDir.resolve(sceneId + ".json");
        String json = Files.readString(file);
        return mapper.readValue(json, GameScene.class);
    }

    // если нужно: listScenes, loadAll, etc.
}