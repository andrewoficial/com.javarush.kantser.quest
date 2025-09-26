package ru.kantser.game.model.state.player;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.state.game.GameState;

import java.util.Map;
import java.util.Set;

public class PlayerState implements PlayerStateInterface {
    private static final Logger log = LoggerFactory.getLogger(PlayerState.class);
    private  String playerId;
    private  Map<String, Integer> resources;
    private  Map<String, Integer> skills;
    private  Set<String> inventory;
    private  String currentScene;
    private  String timeSlot;
    private  int day;
    private  int energy;
    private  int freeMinutes;

    public PlayerState() {
        log.info("Создаю PlayerState без параметров");
    }

    @JsonCreator
    public PlayerState(
            @JsonProperty("playerId") String playerId,
            @JsonProperty("resources") Map<String, Integer> resources,
            @JsonProperty("skills")  Map<String, Integer> skills,
            @JsonProperty("inventory") Set<String> inventory,
            @JsonProperty("currentScene") String currentScene,
            @JsonProperty("timeSlot") String timeSlot,
            @JsonProperty("energy") int energy,
            @JsonProperty("day") int day) {
        log.info("Создаю PlayerState с параметрами");
        this.playerId = playerId;
        log.info("  playerId {}", playerId);

        this.resources = Map.copyOf(resources);
        log.info("  resources {}", resources);

        this.skills = Map.copyOf(skills);
        log.info("  skills {}", skills);

        this.inventory = Set.copyOf(inventory);
        log.info("  inventory {}", inventory);

        this.currentScene = currentScene;
        log.info("  currentScene {}", currentScene);

        this.timeSlot = timeSlot;
        log.info("  timeSlot {}", timeSlot);

        this.day = day;
        log.info("  day {}", day);

        this.energy = energy;
        log.info("  energy {}", energy);
    }

    @Override
    public String getPlayerId() {
        return playerId;
    }

    @Override
    public Map<String, Integer> getResources() {
        return resources;
    }

    @Override
    public Map<String, Integer> getSkills() {
        return skills;
    }

    @Override
    public Set<String> getInventory() {
        return inventory;
    }

    @Override
    public String getCurrentScene() {
        return currentScene;
    }

    @Override
    public String getTimeSlot() {
        return timeSlot;
    }

    @Override
    public int getDay() {
        return day;
    }

    @Override
    public int getEnergy(){
        return energy;
    }

    @Override
    public void setEnergy(int val){
        this.energy = val;
    }

    @Override
    public int getFreeMinutes() {
        return freeMinutes;
    }

    @Override
    public void setFreeMinutes(int val) {
        this.freeMinutes = val;
    }

    @Override
    @JsonIgnore
    public boolean isValid() {
        return playerId != null && !playerId.isEmpty() && day >= 0;
    }

    @Override
    @JsonIgnore
    public void validate() throws IllegalStateException {
        if (!isValid()) {
            throw new IllegalStateException("Invalid PlayerState: playerId must be non-empty, day >= 0");
        }
    }
}