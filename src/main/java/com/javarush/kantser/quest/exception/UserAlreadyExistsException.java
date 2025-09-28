package com.javarush.kantser.quest.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.security.auth.login.CredentialException;

public class UserAlreadyExistsException extends CredentialException {
    private static final Logger log = LoggerFactory.getLogger(UserAlreadyExistsException.class);
    public UserAlreadyExistsException(String message, String login, String pwd) {
        if(login == null){
            log.warn("login is null;");
            login = "NULL";
        }

        if(pwd == null){
            log.warn("pwd is null;");
            pwd = "NULL";
        }

        log.warn("User with credentials name [{}] and password length [{}] already exist! Message: [{}]", login, pwd.length(), message);
    }
}