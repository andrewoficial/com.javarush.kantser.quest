package ru.kantser.game.model.scene.choice;

import java.util.Map;

public class Choice {
    private String id;                 // например "sleep_more"
    private String description;        // текст, который показываем пользователю
    private String nextSceneId;        // куда вести (может быть тот же sceneId для "повторить")
    private Map<String, Object> requires; // опционально: {"minEnergy": 10}
    private Effect effect;            // влияние на player state

    public Choice() {}

    // геттеры/сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getNextSceneId() { return nextSceneId; }
    public void setNextSceneId(String nextSceneId) { this.nextSceneId = nextSceneId; }

    public Map<String, Object> getRequires() { return requires; }
    public void setRequires(Map<String, Object> requires) { this.requires = requires; }

    public Effect getEffect() { return effect; }
    public void setEffect(Effect effect) { this.effect = effect; }
}
