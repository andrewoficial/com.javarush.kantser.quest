package ru.kantser.game.model.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Set;

public class PlayerState {
    private static final Logger log = LoggerFactory.getLogger(PlayerState.class);
    private  String playerId;
    @Getter
    private  Map<String, Integer> resources;
    @Getter
    private  Map<String, Integer> skills;
    @Getter
    private  Set<String> inventory;
    @Getter
    private  String currentScene;
    @Getter
    private  String timeSlot;
    @Getter
    private  int day;
    @Getter
    @Setter
    private  int energy;
    @Getter
    @Setter
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

    @JsonIgnore
    public boolean isValid() {
        return playerId != null && !playerId.isEmpty() && day >= 0;
    }

    @JsonIgnore
    public void validate() throws IllegalStateException {
        if (!isValid()) {
            throw new IllegalStateException("Invalid PlayerState: playerId must be non-empty, day >= 0");
        }
    }
}