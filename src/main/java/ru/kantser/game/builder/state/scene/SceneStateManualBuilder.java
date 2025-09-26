package ru.kantser.game.builder.state.scene;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.state.scene.SceneState;

public class SceneStateManualBuilder implements SceneStateBuilder {
    private static final Logger log = LoggerFactory.getLogger(SceneStateManualBuilder.class);
      private String currentSceneId;

      public SceneStateManualBuilder setCurrentSceneId(String currentSceneId) {
          log.info("Вызываю конструктор SceneState из параметров");
          this.currentSceneId = currentSceneId;
          return this;
      }


    @Override
      public SceneState build() {
          SceneState sceneState = new SceneState();
          sceneState.setCurrentSceneId(currentSceneId);
          return sceneState;
      }
  }
