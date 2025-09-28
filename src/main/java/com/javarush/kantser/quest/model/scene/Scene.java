package com.javarush.kantser.quest.model.scene;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;
import com.javarush.kantser.quest.model.scene.choice.Choice;

@Getter
@Setter
public class Scene {
    private String id;
    private String title;
    private String text;
    private Map<String, Choice> choices;
}