package ru.kantser.game.service.validator.requirement;

import ru.kantser.game.model.state.PlayerState;

import java.util.Map;

public interface RequirementValidator {
    boolean canValidate(Map<String, Object> requirements);
    void validate(Map<String, Object> requirements, PlayerState player, String choiceId);
}