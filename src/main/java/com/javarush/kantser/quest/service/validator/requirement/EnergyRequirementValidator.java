// EnergyRequirementValidator.java
package com.javarush.kantser.quest.service.validator.requirement;

import com.javarush.kantser.quest.model.state.PlayerState;
import java.util.Map;

public class EnergyRequirementValidator implements RequirementValidator {
    
    @Override
    public boolean canValidate(Map<String, Object> requirements) {
        return requirements != null && requirements.containsKey("minEnergy");
    }

    @Override
    public void validate(Map<String, Object> requirements, PlayerState player, String choiceId) {
        Object minEnergyObj = requirements.get("minEnergy");
        if (minEnergyObj instanceof Number) {
            int minEnergy = ((Number) minEnergyObj).intValue();
            if (player.getEnergy() < minEnergy) {
                throw new IllegalStateException("Not enough energy for choice: " + choiceId);
            }
        }
    }
}