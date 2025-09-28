package com.javarush.kantser.quest.service.validator.requirement;

import com.javarush.kantser.quest.model.state.PlayerState;

import java.util.Map;

public interface RequirementValidator {
    boolean canValidate(Map<String, Object> requirements);
    void validate(Map<String, Object> requirements, PlayerState player, String choiceId);
}