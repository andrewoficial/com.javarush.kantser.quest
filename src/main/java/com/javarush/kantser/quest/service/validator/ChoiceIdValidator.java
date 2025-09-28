package com.javarush.kantser.quest.service.validator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.javarush.kantser.quest.exception.NoChooseParameter;

import java.io.IOException;

public class ChoiceIdValidator {

    public void checkParameterChoice(HttpServletRequest req, HttpServletResponse resp, String choiceId, String userId, String commentary ) throws NoChooseParameter, IOException {
        if (choiceId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, commentary);
            throw new NoChooseParameter(commentary, userId, null);
        }else{
            String cleanChoice = choiceId.trim();
            if(cleanChoice.isEmpty()){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, commentary);
                throw new NoChooseParameter(commentary, userId, choiceId);
            }
        }
    }
}
