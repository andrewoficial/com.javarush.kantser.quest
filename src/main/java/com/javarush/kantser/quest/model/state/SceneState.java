package com.javarush.kantser.quest.model.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.javarush.kantser.quest.model.scene.Scene;

public class SceneState {
    private static final Logger log = LoggerFactory.getLogger(SceneState.class);
    @Setter
    @Getter
    private String currentSceneId;
    @Setter
    private transient Scene currentScene; // transient - не сериализуется в JSON
    private transient String tipText = ""; //лень делать много проверок на нул

    public SceneState() {
        log.info("Создаю SceneState без параметров");
    }

    @JsonCreator
    public SceneState(@JsonProperty("currentSceneId") String currentSceneId) {
        log.info("Создаю SceneState с параметрами");
        this.currentSceneId = currentSceneId;
    }

    @JsonIgnore
    public Scene getCurrentScene() {
        return currentScene;
    }

    @JsonIgnore
    public void setTipText(String text){
        tipText = text;
    }

    @JsonIgnore
    public String getTipText(){
        return tipText;
    }

    @Override
    public String toString() {
        return "SceneState{currentSceneId='" + currentSceneId + "', currentScene=" + currentScene + "}";
    }
}