package ru.kantser.game.model.state.player;

import java.util.Map;
import java.util.Set;

public interface PlayerStateInterface {
    String getPlayerId();

    Map<String, Integer> getResources();

    Map<String, Integer> getSkills();

    Set<String> getInventory();

    String getCurrentScene();

    String getTimeSlot();

    int getDay();

    boolean isValid();

    void validate() throws IllegalStateException;

    int getEnergy();
    
    void setEnergy(int val);

    int getFreeMinutes();

    void setFreeMinutes(int val);
}