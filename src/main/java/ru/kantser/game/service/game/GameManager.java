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
import java.io.IOException;


@Getter
public class GameManager {
    private static final Logger log = LoggerFactory.getLogger(GameManager.class);
    private SceneJsonBuilder sceneBuilder;
    private final EffectApplier effectApplier;

    public GameManager(String scenesPath) {
        SceneJsonBuilder sceneBuilder = new SceneJsonBuilder(scenesPath);
        this.effectApplier = new DefaultEffectApplier();
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
        Scene scene = sceneBuilder.getScene(sceneState.getCurrentSceneId());
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

        if (choice.getEffect() != null) {
            effectApplier.apply(choice.getEffect(), state.getPlayerState());
        }

        String nextSceneId;
        if(player.getEnergy() < 10 || player.getFreeMinutes() < -50){
            nextSceneId = "badEnding";
        }else{
            nextSceneId = choice.getNextSceneId();
        }



        if (nextSceneId != null && !nextSceneId.isBlank()) {
            SceneState newSceneState = new SceneState();
            newSceneState.setCurrentSceneId(nextSceneId);
            if(choice.getEffect()  != null){
                newSceneState.setTipText(choice.getEffect().getNote());
            }else{
                newSceneState.setTipText("");
            }

            state.setSceneState(newSceneState);
        }

        return state;
    }

}