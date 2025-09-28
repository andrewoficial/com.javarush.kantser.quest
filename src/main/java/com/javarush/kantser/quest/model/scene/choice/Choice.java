package com.javarush.kantser.quest.model.scene.choice;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Choice {
    private String id;
    private String description;
    private String nextSceneId;
    private Map<String, Object> requires;
    private Effect effect;
}
