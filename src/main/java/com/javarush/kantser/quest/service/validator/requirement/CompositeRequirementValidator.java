// CompositeRequirementValidator.java
package com.javarush.kantser.quest.service.validator.requirement;

import com.javarush.kantser.quest.model.scene.choice.Choice;
import com.javarush.kantser.quest.model.state.PlayerState;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompositeRequirementValidator {
    private final List<RequirementValidator> validators;

    public CompositeRequirementValidator() {
        this.validators = new ArrayList<>();
        // Добавляем валидаторы по умолчанию
        this.validators.add(new EnergyRequirementValidator());
        this.validators.add(new SkillRequirementValidator());
        this.validators.add(new ResourceRequirementValidator());
    }

    public CompositeRequirementValidator(List<RequirementValidator> validators) {
        this.validators = new ArrayList<>(validators);
    }

    public void addValidator(RequirementValidator validator) {
        this.validators.add(validator);
    }

    public void validate(Choice choice, PlayerState player) {
        if (choice.getRequires() == null) {
            return;
        }

        Map<String, Object> requirements = choice.getRequires();
        
        for (RequirementValidator validator : validators) {
            if (validator.canValidate(requirements)) {
                validator.validate(requirements, player, choice.getId());
            }
        }
    }
}