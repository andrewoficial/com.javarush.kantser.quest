package ru.kantser.game.builder.scene;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.scene.GameScene;
import ru.kantser.game.model.scene.Scene;
import ru.kantser.game.model.scene.choice.Choice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON билдер для Scene
 */
public class SceneJsonBuilder implements SceneBuilder {
    private static final Logger log = LoggerFactory.getLogger(SceneJsonBuilder.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String jsonSource;
    private String id;
    private String title;
    private String text;
    private Map<String, Choice> choices = new HashMap<>();

    private final Path scenesDir;
    private final ObjectMapper mapper = new ObjectMapper();

    public SceneJsonBuilder(String scenesPath) {
        this.scenesDir = Path.of(scenesPath);
    }

    public GameScene getScene(String sceneId) throws IOException {
        Path file = scenesDir.resolve(sceneId + ".json");
        String json = Files.readString(file);
        return mapper.readValue(json, GameScene.class);
    }

    @Override
    public SceneJsonBuilder id(String id) {
        this.id = id;
        return this;
    }

    @Override
    public SceneJsonBuilder title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public SceneJsonBuilder text(String text) {
        this.text = text;
        return this;
    }

    @Override
    public SceneJsonBuilder choice(String key, Choice value) {
        this.choices.put(key, value);
        return this;
    }

    @Override
    public SceneJsonBuilder choices(Map<String, Choice> choices) {
        this.choices.putAll(choices);
        return this;
    }

    @Override
    public Scene build() {
        if (jsonSource != null && !jsonSource.trim().isEmpty()) {
            try {
                Scene scene = objectMapper.readValue(jsonSource, Scene.class);
                // Переопределяем значения если они заданы
                if (id != null) scene.setId(id);
                if (title != null) scene.setTitle(title);
                if (text != null) scene.setText(text);
                if (!choices.isEmpty()) {
                    if (scene.getChoices() == null) {
                        scene.setChoices(new HashMap<>());
                    }
                    scene.getChoices().putAll(choices);
                }
                return scene;
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse Scene from JSON", e);
            }
        }

        // Создаем вручную
        Scene scene = new Scene();
        scene.setId(id);
        scene.setTitle(title);
        scene.setText(text);
        scene.setChoices(new HashMap<>(choices));
        return scene;
    }
}