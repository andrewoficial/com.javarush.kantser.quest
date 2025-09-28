// SkillRequirementValidator.java
package com.javarush.kantser.quest.service.validator.requirement;

import com.javarush.kantser.quest.model.state.PlayerState;
import java.util.Map;

public class SkillRequirementValidator implements RequirementValidator {
    
    @Override
    public boolean canValidate(Map<String, Object> requirements) {
        return requirements != null && requirements.containsKey("minSkill");
    }

    @Override
    public void validate(Map<String, Object> requirements, PlayerState player, String choiceId) {
        Object minSkillObj = requirements.get("minSkill");
        if (minSkillObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Number> skillRequirements = (Map<String, Number>) minSkillObj;
            
            for (Map.Entry<String, Number> entry : skillRequirements.entrySet()) {
                String skillName = entry.getKey();
                int requiredLevel = entry.getValue().intValue();
                Integer playerSkillLevel = player.getSkills().get(skillName);
                
                if (playerSkillLevel == null || playerSkillLevel < requiredLevel) {
                    throw new IllegalStateException(
                        "Skill requirement not met for choice " + choiceId + 
                        ": need " + requiredLevel + " in " + skillName
                    );
                }
            }
        }
    }
}