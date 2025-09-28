package ru.kantser.game.service.game;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.scene.Scene;
import ru.kantser.game.model.scene.choice.Choice;
import ru.kantser.game.model.state.GameState;
import ru.kantser.game.model.state.PlayerState;
import ru.kantser.game.model.state.SceneState;
import ru.kantser.game.service.files.SceneJsonBuilder;
import ru.kantser.game.service.validator.GameStateValidator;
import ru.kantser.game.service.validator.requirement.CompositeRequirementValidator;
import java.io.IOException;

@Getter
public class GameManager {
    private static final Logger log = LoggerFactory.getLogger(GameManager.class);
    private SceneJsonBuilder sceneBuilder;
    private final EffectApplier effectApplier;
    private final GameStateValidator gameStateValidator;
    private final CompositeRequirementValidator requirementsValidator;
    private final NextSceneResolver nextSceneResolver;

    public GameManager(String scenesPath) {
        SceneJsonBuilder sceneBuilder = new SceneJsonBuilder(scenesPath);
        this.effectApplier = new DefaultEffectApplier();
        this.requirementsValidator = new CompositeRequirementValidator();
        this.nextSceneResolver = new NextSceneResolver();
        log.info("Created SceneService with folder: {}", scenesPath);
        log.info("Created SceneService: {}", sceneBuilder.toString());
        this.setSceneBuilder(sceneBuilder);
        log.info("GameManager initialized (scenesPath)");
        this.gameStateValidator = new GameStateValidator(this.sceneBuilder);
    }

    public void setSceneBuilder(SceneJsonBuilder sceneBuilder) {
        if (sceneBuilder == null) {
            log.warn("Passed null SceneService to setSceneService method");
        }
        this.sceneBuilder = sceneBuilder;
    }

    public GameState processChoice(GameState state, String choiceId) throws IOException {
        log.info("Applying choice result to player and scene. Choice ID: {}", choiceId);

        gameStateValidator.validateGameState(state);

        SceneState sceneState = state.getSceneState();
        PlayerState player = state.getPlayerState();

        Scene scene = gameStateValidator.validateAndLoadScene(sceneState.getCurrentSceneId());
        Choice choice = getValidatedChoice(scene, choiceId);

        requirementsValidator.validate(choice, player);
        applyChoiceEffects(choice, player);
        updateGameState(state, choice, player);

        return state;
    }

    private Choice getValidatedChoice(Scene scene, String choiceId) {
        Choice choice = scene.getChoices().get(choiceId);
        if (choice == null) {
            throw new IllegalArgumentException("Unknown choice: " + choiceId);
        }
        log.info("Retrieved selected action to apply impact: {}", choice.getId());
        return choice;
    }

    private void applyChoiceEffects(Choice choice, PlayerState player) {
        if (choice.getEffect() != null) {
            effectApplier.apply(choice.getEffect(), player);
        }
    }

    private void updateGameState(GameState state, Choice choice, PlayerState player) {
        String nextSceneId = nextSceneResolver.resolveNextScene(choice, player);

        if (nextSceneId != null && !nextSceneId.isBlank()) {
            SceneState newSceneState = createNewSceneState(choice, nextSceneId);
            state.setSceneState(newSceneState);
        }
    }

    private SceneState createNewSceneState(Choice choice, String nextSceneId) {
        SceneState newSceneState = new SceneState();
        newSceneState.setCurrentSceneId(nextSceneId);

        String tipText = choice.getEffect() != null ? choice.getEffect().getNote() : "";
        newSceneState.setTipText(tipText);

        return newSceneState;
    }
}