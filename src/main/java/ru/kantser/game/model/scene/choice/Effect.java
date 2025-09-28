package ru.kantser.game.model.scene.choice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Effect {
    private Integer freeMinutesDelta;
    private Integer energyDelta;
    private String note;
}
