package ru.kantser.game.constatnt;

public interface AppConstant {
    String DEFAULT_SAVE_NAME = "auto";

    interface FileExtensions{
        String FILE_EXTENSION_DOT_JSON = ".json";
    }

    interface AppLinks{
        String FILE_HTML_LOGIN = "login.html";
        String FILE_JSP_GAME = "/WEB-INF/JSP/game.jsp";
        String FILE_JSP_LIST_SAVES = "/WEB-INF/JSP/list-saves.jsp";
        String PATH_TO_JSON_SCENES = "/WEB-INF/scenes";
        String PATH_TO_USER_SAVES = "/saves";
    }

    interface SessionKeys{
        /* Do not delete  [static final] for better remaindering */
        static final String SESSION_KEY_GAME_STATE = "gameState";
        static final String SESSION_KEY_PENDING_LOAD_USER = "pendingLoadUser";
        static final String SESSION_KEY_PENDING_LOAD_SLOT = "pendingLoadSlot";
    }
    interface RequestAttrNames{
        String SAVES = "saves";
        String USER_ID = "userId";
        String GAME_STATE = "gameState";
        String ACTION = "action";
        String ACTION_TYPE = "actionType";
        String CHOICE_ID = "choiceId";
        interface ActionsType{
            String LOAD = "load";
            String SHOW = "show";
            String LIST_SAVES = "listSaves";
            String NEW = "new";
            String CHOICE = "choice";
            String SERVER_COMMAND = "serverCommand";
        }
    }
}
