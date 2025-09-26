package ru.kantser.game.builder.scene;

import ru.kantser.game.model.scene.Scene;
import ru.kantser.game.model.scene.choice.Choice;

import java.util.Map;

public interface SceneBuilder {
    SceneBuilder id(String id);
    SceneBuilder title(String title);
    SceneBuilder text(String text);
    SceneBuilder choice(String key, Choice value);

    SceneBuilder choices(Map<String, Choice> choices);

    Scene build();
}