package ru.kantser.game.builder.scene;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.scene.Scene;
import ru.kantser.game.model.scene.choice.Choice;

import java.util.HashMap;
import java.util.Map;

public class SceneManualBuilder implements SceneBuilder {
    private static final Logger log = LoggerFactory.getLogger(SceneManualBuilder.class);
    private String id;
    private String title;
    private String text;
    private Map<String, Choice> choices = new HashMap<>();

    @Override
    public SceneManualBuilder id(String id) {
        this.id = id;
        return this;
    }

    @Override
    public SceneManualBuilder title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public SceneManualBuilder text(String text) {
        this.text = text;
        return this;
    }

    @Override
    public SceneManualBuilder choice(String key, Choice value) {
        this.choices.put(key, value);
        return this;
    }

    @Override
    public SceneBuilder choices(Map<String, Choice> choices) {
        this.choices.putAll(choices);
        return this;
    }

    @Override
    public Scene build() {
        log.info("Вызываю сборку Scene из параметров");
        Scene scene = new Scene();
        scene.setId(id);
        scene.setTitle(title);
        scene.setText(text);
        scene.setChoices(new HashMap<>(choices));
        return scene;
    }
}