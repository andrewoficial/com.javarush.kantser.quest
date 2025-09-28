package ru.kantser.game.service.files;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.state.SceneState;

public class SceneStateJsonBuilder {
    private static final Logger log = LoggerFactory.getLogger(SceneStateJsonBuilder.class);
    private String json;

  public SceneStateJsonBuilder(String json) {
      log.info("Вызываю конструктор SceneState из JSON");
      this.json = json;
  }


  public SceneState build() {
      ObjectMapper mapper = new ObjectMapper();
      try {
          return mapper.readValue(json, SceneState.class);
      } catch (JsonProcessingException e) {
          throw new RuntimeException("Failed to parse JSON", e);
      }
  }
}