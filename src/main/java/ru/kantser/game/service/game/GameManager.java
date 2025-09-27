package ru.kantser.game.service.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.scene.GameScene;
import ru.kantser.game.model.scene.choice.Choice;
import ru.kantser.game.model.scene.choice.Effect;
import ru.kantser.game.model.state.game.GameState;
import ru.kantser.game.model.state.player.PlayerState;
import ru.kantser.game.model.state.scene.SceneState;
import ru.kantser.game.builder.scene.SceneJsonBuilder;
import java.io.IOException;


public class GameManager {
    private static final Logger log = LoggerFactory.getLogger(GameManager.class);
    private SceneJsonBuilder sceneBuilder;

    public GameManager() {
        log.info("GameManager инициализирован (без параметров)");
    }

    public GameManager(String scenesPath) {
        this();
        SceneJsonBuilder sceneBuilder = new SceneJsonBuilder(scenesPath);
        log.info("Создан SceneService с папкой {}", scenesPath);
        log.info("Созданный SceneService {}", sceneBuilder.toString());
        this.setSceneBuilder(sceneBuilder);
        log.info("GameManager инициализирован (scenesPath)");
    }

    public void setSceneBuilder(SceneJsonBuilder sceneBuilder){
        if(sceneBuilder == null){
            log.warn("Передал null SceneService в метод setSceneService");
        }
        this.sceneBuilder = sceneBuilder;
    }

    public GameState processChoice(GameState state, String choiceId) throws IOException {
        log.info("Применяю результат выбора к игроку и сцене choiceId{}", choiceId);
        if (state == null) throw new IllegalArgumentException("state == null");
        if (choiceId == null) throw new IllegalArgumentException("choiceId == null");

        SceneState sceneState = state.getSceneState();
        PlayerState player = state.getPlayerState();

        if (sceneState == null || sceneState.getCurrentSceneId() == null) {
            throw new IllegalStateException("No current scene");
        }

        // Получаем сцену (если в state не вложена, загружаем через sceneService)
        GameScene scene = sceneBuilder.getScene(sceneState.getCurrentSceneId());
        if (scene == null) throw new IllegalStateException("Scene not found: " + sceneState.getCurrentSceneId());
        log.info("Получил сцену, над которой надо применить действие {}", scene.getTitle());
        Choice choice = scene.getChoices().get(choiceId);
        if (choice == null) throw new IllegalArgumentException("Unknown choice: " + choiceId);
        log.info("Получил выбранное действие, импакт которого надо применить {}", choice.getId());
        // Проверка требований (requires) — простой пример:
        if (choice.getRequires() != null) {
            Object minEnergyObj = choice.getRequires().get("minEnergy");
            if (minEnergyObj instanceof Number) {
                int minEnergy = ((Number) minEnergyObj).intValue();
                if (player.getEnergy() < minEnergy) {
                    // нельзя выбрать — оставляем state без изменений или кидаем исключение
                    throw new IllegalStateException("Недостаточно энергии для выбора " + choiceId);
                }
            }
            // сюда можно добавить другие проверки
        }

        // Применяю effect
        Effect e = choice.getEffect();
        if (e != null) {
            if (e.getFreeMinutesDelta() != null) {
                int newMinutes = player.getFreeMinutes() + e.getFreeMinutesDelta();
                player.setFreeMinutes(Math.max(0, newMinutes));
            }
            log.info("player.getEnergy() до применения {}",player.getEnergy());
            if (e.getEnergyDelta() != null) {
                log.info("e.getEnergyDelta() {}",e.getEnergyDelta());
                int newEnergy = player.getEnergy() + e.getEnergyDelta();
                player.setEnergy(Math.max(0, Math.min(100, newEnergy))); // clamp 0..100
                state.setPlayerState(player);//Вроде не обновляло
            }
            log.info("player.getEnergy() после применения {}",player.getEnergy());

            // можно логировать e.getNote()
        }

        // Смена сцены
        String nextScene = choice.getNextSceneId();
        if (nextScene != null && !nextScene.isBlank()) {
            SceneState newSceneState = new SceneState();
            newSceneState.setCurrentSceneId(nextScene);
            state.setSceneState(newSceneState);
        } else {
            // если nextScene == null, оставляем ту же сцену
        }

        return state;
    }

    public SceneJsonBuilder getSceneBuilder(){
        return sceneBuilder;
    }
}