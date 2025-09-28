package com.javarush.kantser.quest.service.validator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.javarush.kantser.quest.exception.NoChooseParameter;
import com.javarush.kantser.quest.model.scene.Scene;
import com.javarush.kantser.quest.model.scene.choice.Choice;

import java.io.IOException;

public class ChoiceValidator {

    public void validateChoiceParameter(HttpServletRequest req, HttpServletResponse resp, String choiceId, String userId, String commentary) 
            throws NoChooseParameter, IOException {
        if (choiceId == null || choiceId.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, commentary);
            throw new NoChooseParameter(commentary, userId, choiceId);
        }
    }

    public Choice validateAndGetChoice(Scene scene, String choiceId) {
        if (scene == null) {
            throw new IllegalArgumentException("Scene cannot be null");
        }
        
        if (choiceId == null || choiceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Choice ID cannot be null or empty");
        }
        
        Choice choice = scene.getChoices().get(choiceId);
        if (choice == null) {
            throw new IllegalArgumentException("Unknown choice: " + choiceId);
        }
        
        return choice;
    }
}