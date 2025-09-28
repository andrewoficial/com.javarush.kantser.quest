package com.javarush.kantser.quest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.javarush.kantser.quest.constatnt.AppConstant;
import com.javarush.kantser.quest.exception.NoChooseParameter;
import com.javarush.kantser.quest.model.scene.Scene;
import com.javarush.kantser.quest.model.state.GameState;
import com.javarush.kantser.quest.model.state.PlayerState;
import com.javarush.kantser.quest.model.state.SceneState;
import com.javarush.kantser.quest.service.game.GameManager;
import com.javarush.kantser.quest.service.game.SaveGameManager;
import com.javarush.kantser.quest.service.validator.ChoiceIdValidator;
import com.javarush.kantser.quest.service.validator.GameStateValidator;

import java.io.IOException;
import java.util.*;

@WebServlet("/game")
public class GameServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(GameServlet.class);

    private GameManager gameManager;
    private SaveGameManager saveManager;
    private ChoiceIdValidator choiceIdValidator;
    private GameStateValidator objValidator;

    @Override
    public void init() {
        log.info("init");
        String savePath = getServletContext().getRealPath(AppConstant.AppLinks.PATH_TO_USER_SAVES);
        String scenesPath = getServletContext().getRealPath(AppConstant.AppLinks.PATH_TO_JSON_SCENES);
        this.saveManager = new SaveGameManager(savePath);
        this.gameManager = new GameManager(scenesPath);
        this.choiceIdValidator = new ChoiceIdValidator();
        this.objValidator = new GameStateValidator(this.gameManager.getSceneBuilder());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("doGet");
        String userId = (String) req.getSession().getAttribute(AppConstant.RequestAttrNames.USER_ID);
        if (userId == null) {
            resp.sendRedirect(AppConstant.AppLinks.FILE_HTML_LOGIN);
            return;
        }

        String action = req.getParameter(AppConstant.RequestAttrNames.ACTION);
        if(action == null){
            log.warn("Empty [action] property");
        }
        try {
            switch (action != null ? action : AppConstant.RequestAttrNames.ActionsType.SHOW) { // Default to show
                case AppConstant.RequestAttrNames.ActionsType.LOAD:
                    loadGame(req, resp);
                    break;
                case AppConstant.RequestAttrNames.ActionsType.SHOW:
                    showCurrentGame(req, resp, userId);
                    break;
                case AppConstant.RequestAttrNames.ActionsType.LIST_SAVES:
                    listSaves(req, resp, userId);
                    break;
                case AppConstant.RequestAttrNames.ActionsType.NEW:
                    newGame(req, resp, userId);
                    break;
                case AppConstant.RequestAttrNames.ActionsType.CHOICE:
                    String choiceId = req.getParameter(AppConstant.RequestAttrNames.CHOICE_ID);
                    processChoice(req, resp, userId, choiceId);
                    break;
                default:
                    log.warn("Load default-case (current game), because  action is {}, ", action);
                    showCurrentGame(req, resp, userId);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        log.info("doPost" );
        String userId = (String) req.getSession().getAttribute(AppConstant.RequestAttrNames.USER_ID);
        if (userId == null) {
            resp.sendRedirect(AppConstant.AppLinks.FILE_HTML_LOGIN);
            return;
        }

        String actionType = req.getParameter(AppConstant.RequestAttrNames.ACTION_TYPE);
        String choiceId = req.getParameter(AppConstant.RequestAttrNames.CHOICE_ID);

        if (AppConstant.RequestAttrNames.ActionsType.CHOICE.equals(actionType)) {
            handleChoicePost(req, resp, userId, choiceId);
        } else if (AppConstant.RequestAttrNames.ActionsType.SERVER_COMMAND.equals(actionType)) {
            handleServerCommandPost(req, resp, userId, req.getParameter(AppConstant.RequestAttrNames.CHOICE_ID));
        } else {
            handleChoicePost(req, resp, userId, choiceId);
        }
    }

    private void handleChoicePost(HttpServletRequest req, HttpServletResponse resp, String userId, String choiceId) throws IOException, NoChooseParameter {
        log.info("handleChoicePost user={} choice={}", userId, choiceId);
        choiceIdValidator.checkParameterChoice(req, resp, choiceId, userId, "handleChoicePost");

        GameState currentState = saveManager.load(userId, AppConstant.DEFAULT_SAVE_NAME);
        if (currentState == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No current game");
            return;
        }

        GameState updatedState = gameManager.processChoice(currentState, choiceId);
        saveManager.save(updatedState, userId, AppConstant.DEFAULT_SAVE_NAME);

        req.getSession().setAttribute(AppConstant.SessionKeys.SESSION_KEY_GAME_STATE, updatedState);

        resp.sendRedirect(req.getContextPath() + "/game?action=show");
    }


    private void handleServerCommandPost(HttpServletRequest req, HttpServletResponse resp, String userId, String command) throws IOException {
        log.info("handleServerCommandPost user={} command={}", userId, command);

        String username = req.getParameter("username");
        String slotName = req.getParameter("slotName");
        if (username != null && slotName != null) {
            req.getSession().setAttribute(AppConstant.SessionKeys.SESSION_KEY_PENDING_LOAD_USER, username);
            req.getSession().setAttribute(AppConstant.SessionKeys.SESSION_KEY_PENDING_LOAD_SLOT, slotName);
        }
        resp.sendRedirect(req.getContextPath() + "/game?action=show");
    }

    private void loadGame(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String username = req.getParameter("username");
        String slotName = req.getParameter("slotName");

        if (username == null || slotName == null) {
            throw new IllegalArgumentException("saveId required");
        }
        GameState gameState = saveManager.load(username, slotName);
        req.setAttribute(AppConstant.RequestAttrNames.GAME_STATE, gameState);
        req.getRequestDispatcher(AppConstant.AppLinks.FILE_JSP_GAME).forward(req, resp);
    }

    private void listSaves(HttpServletRequest req, HttpServletResponse resp, String userId) throws ServletException, IOException {
        List<String> saves = saveManager.listSaves(userId);
        req.setAttribute(AppConstant.RequestAttrNames.SAVES, saves);
        log.info("Redirect to saves");
        req.getRequestDispatcher(AppConstant.AppLinks.PATH_TO_USER_SAVES).forward(req, resp);
    }

    private void newGame(HttpServletRequest req, HttpServletResponse resp, String userId) throws IOException, ServletException {
        log.info("Run create new game for user: {}", userId );

        GameState newState = new GameState();
        log.info("Create gameState");

        Map<String, Integer> resources = new HashMap<>();
        Map<String, Integer> skills = new HashMap<>();
        Set<String> inventory = new HashSet<>();

        PlayerState playerState = new PlayerState(userId, resources, skills, inventory, "start", "", 85, 1);
        newState.setPlayerState(playerState);
        log.info("Create playerState and put it in gameState");


        Scene firstScene = gameManager.getSceneBuilder().getScene("start");
        SceneState sceneState = new SceneState();
        sceneState.setCurrentSceneId("start");
        sceneState.setCurrentScene(firstScene);

        newState.setSceneState(sceneState);
        log.info("Create sceneState and put it in gameState");

        // Auto-save
        saveManager.save(newState, userId, AppConstant.DEFAULT_SAVE_NAME);

        req.setAttribute("gameState", newState);
        req.getRequestDispatcher(AppConstant.AppLinks.FILE_JSP_GAME).forward(req, resp);
    }

    private void showCurrentGame(HttpServletRequest req, HttpServletResponse resp, String userId) throws IOException, ServletException {
        log.info("showCurrentGame");
        Object sessionState = req.getSession().getAttribute(AppConstant.SessionKeys.SESSION_KEY_GAME_STATE);
        GameState gameState;

        String pendingUser = (String) req.getSession().getAttribute(AppConstant.SessionKeys.SESSION_KEY_PENDING_LOAD_USER);
        String pendingSlot = (String) req.getSession().getAttribute(AppConstant.SessionKeys.SESSION_KEY_PENDING_LOAD_SLOT);
        log.info("pendingUser {}, pendingSlot {}", pendingUser, pendingSlot);
        if (pendingUser != null && pendingSlot != null) { //Если данные для сохранения передаются полями запроса
            log.info("Found pending load: {}/{}", pendingUser, pendingSlot);
            gameState = saveManager.load(pendingUser, pendingSlot);
            log.info(gameState.getSceneState().getCurrentScene().getChoices().toString());
            log.info(gameState.getSceneState().getCurrentScene().getTitle());

            req.getSession().removeAttribute(AppConstant.SessionKeys.SESSION_KEY_PENDING_LOAD_USER);
            req.getSession().removeAttribute(AppConstant.SessionKeys.SESSION_KEY_PENDING_LOAD_SLOT);

            req.getSession().setAttribute(AppConstant.RequestAttrNames.USER_ID, pendingUser);
            req.getSession().setAttribute(AppConstant.SessionKeys.SESSION_KEY_GAME_STATE, gameState);
        } else if (sessionState instanceof GameState) {
            log.info("Found game state (obj)");
            gameState = (GameState) sessionState;
            objValidator.ensureSceneLoaded(gameState);
            req.setAttribute(AppConstant.RequestAttrNames.GAME_STATE, gameState);
            req.getRequestDispatcher(AppConstant.AppLinks.FILE_JSP_GAME).forward(req, resp);
        } else {
            log.warn("pendingUser or pendingSlot is NULL  or received not instance of GameState, load last autosave");
            if(saveManager.exists(userId, AppConstant.DEFAULT_SAVE_NAME)){
                gameState = saveManager.load(userId, AppConstant.DEFAULT_SAVE_NAME);
                if (gameState == null) {
                    log.warn("Can not load autosave, run new game (by redirect)");
                    resp.sendRedirect(req.getContextPath() + "/game?action=new");
                }else{
                    objValidator.ensureSceneLoaded(gameState);
                    req.setAttribute(AppConstant.RequestAttrNames.GAME_STATE, gameState);
                    req.getRequestDispatcher(AppConstant.AppLinks.FILE_JSP_GAME).forward(req, resp);
                }
            }else{
                log.warn("Autosave not found. Run new game (by redirect)");
                resp.sendRedirect(req.getContextPath() + "/game?action=new");
            }
        }

    }

    private void processChoice(HttpServletRequest req, HttpServletResponse resp, String userId, String choiceId) throws IOException, ServletException, NoChooseParameter {
        log.info("Run user choice: {}", choiceId );
        choiceIdValidator.checkParameterChoice(req, resp, choiceId, userId, "processChoice");
        GameState currentState = saveManager.load(userId, AppConstant.DEFAULT_SAVE_NAME);
        if (currentState == null) {
            throw new IllegalStateException("No current game");
        }

        GameState updatedState = gameManager.processChoice(currentState, choiceId);

        objValidator.ensureSceneLoaded(updatedState);
        saveManager.save(updatedState, userId, AppConstant.DEFAULT_SAVE_NAME);

        req.setAttribute(AppConstant.RequestAttrNames.GAME_STATE, updatedState);
        req.getRequestDispatcher(AppConstant.AppLinks.FILE_JSP_GAME).forward(req, resp);
    }


}