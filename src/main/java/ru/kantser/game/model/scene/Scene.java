package ru.kantser.game.model.scene;

import ru.kantser.game.model.scene.choice.Choice;

import java.util.Map;

public class Scene implements GameScene {
    private String id;
    private String title;
    private String text;
    private Map<String, Choice> choices;

    public Scene() {}

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Map<String, Choice> getChoices() {
        return choices;
    }

    public void setChoices(Map<String, Choice> choices) {
        this.choices = choices;
    }
}