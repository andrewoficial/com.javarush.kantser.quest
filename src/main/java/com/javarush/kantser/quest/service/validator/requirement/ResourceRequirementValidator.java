// ResourceRequirementValidator.java
package com.javarush.kantser.quest.service.validator.requirement;

import com.javarush.kantser.quest.model.state.PlayerState;
import java.util.Map;

public class ResourceRequirementValidator implements RequirementValidator {
    
    @Override
    public boolean canValidate(Map<String, Object> requirements) {
        return requirements != null && requirements.containsKey("resources");
    }

    @Override
    public void validate(Map<String, Object> requirements, PlayerState player, String choiceId) {
        Object resourcesObj = requirements.get("resources");
        if (resourcesObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Number> resourceRequirements = (Map<String, Number>) resourcesObj;
            
            for (Map.Entry<String, Number> entry : resourceRequirements.entrySet()) {
                String resourceName = entry.getKey();
                int requiredAmount = entry.getValue().intValue();
                Integer playerResourceAmount = player.getResources().get(resourceName);
                
                if (playerResourceAmount == null || playerResourceAmount < requiredAmount) {
                    throw new IllegalStateException(
                        "Resource requirement not met for choice " + choiceId + 
                        ": need " + requiredAmount + " " + resourceName
                    );
                }
            }
        }
    }
}