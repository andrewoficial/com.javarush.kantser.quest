package ru.kantser.game.model.scene;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.kantser.game.model.scene.choice.Choice;

import java.util.Map;

// Интерфейс для сцены
@JsonDeserialize(as = ru.kantser.game.model.scene.Scene.class)
public interface GameScene {
    String getId();
    String getTitle();
    String getText();
    Map<String, Choice> getChoices();

}