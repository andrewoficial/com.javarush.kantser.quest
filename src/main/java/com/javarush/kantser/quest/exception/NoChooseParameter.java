package com.javarush.kantser.quest.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.CredentialException;

public class NoChooseParameter extends CredentialException {
    private static final Logger log = LoggerFactory.getLogger(NoChooseParameter.class);
    public NoChooseParameter(String message, String userId, String choiceId) {
        if(userId == null){
            log.warn("userId is null;");
            userId = "NULL";
        }

        if(userId.isEmpty()){
            log.warn("userId is empty;");
            userId = "EMPTY";
        }

        if(choiceId == null){
            log.warn("choiceId is null;");
            choiceId = "NULL";
        }

        if(choiceId.isEmpty()){
            log.warn("choiceId is empty;");
            choiceId = "EMPTY";
        }

        log.warn("Received wrong choiceId userId [{}] and choiceId [{}]! Message: [{}]", userId, choiceId, message);
    }

}
