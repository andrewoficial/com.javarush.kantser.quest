package com.javarush.kantser.quest.service.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.javarush.kantser.quest.model.scene.choice.Choice;
import com.javarush.kantser.quest.model.state.PlayerState;


public class NextSceneResolver {
    private static final Logger log = LoggerFactory.getLogger(NextSceneResolver.class);

    public String resolveNextScene(Choice choice, PlayerState player) {
        if (isBadEndingCondition(player)) {
            return "badEnding";
        }
        return choice.getNextSceneId();
    }

    private boolean isBadEndingCondition(PlayerState player) {
        return player.getEnergy() < 10 || player.getFreeMinutes() < -50;
    }
}