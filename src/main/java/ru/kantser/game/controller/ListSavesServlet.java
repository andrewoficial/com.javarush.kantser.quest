package ru.kantser.game.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.constatnt.AppConstant;
import ru.kantser.game.model.state.GameState;
import ru.kantser.game.service.game.GameManager;
import ru.kantser.game.service.game.SaveGameManager;
import ru.kantser.game.service.validator.GameStateValidator;


import java.io.IOException;

@WebServlet("/saves")
public class ListSavesServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ListSavesServlet.class);
    private SaveGameManager saveManager;
    private GameManager gameManager;
    private GameStateValidator gameStateValidator;

    @Override
    public void init() {
        log.info("init");
        String savePath = getServletContext().getRealPath(AppConstant.AppLinks.PATH_TO_USER_SAVES);
        String scenesPath = getServletContext().getRealPath(AppConstant.AppLinks.PATH_TO_JSON_SCENES);
        this.saveManager = new SaveGameManager(savePath);
        this.gameManager = new GameManager(scenesPath);
        this.gameStateValidator = new GameStateValidator(this.gameManager.getSceneBuilder());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.info("doPost");
        String username = req.getParameter("username");
        String action = req.getParameter("action");
        String slotName = req.getParameter("slotName");

        if (username == null || action == null || slotName == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            if ("remove".equals(action)) {
                processPostActionRemove(req, resp, username, slotName);
            } else if ("load".equals(action)) {
                processPostActionLoad(req, resp, username, slotName);
            } else if ("save".equals(action)) {
                processPostActionSave(req, resp, username, slotName);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.info("doGet" );
        String userId = (String) req.getSession().getAttribute(AppConstant.RequestAttrNames.USER_ID);
        if (userId == null) {
            resp.sendRedirect("login.html");
            return;
        }
        String action = req.getParameter("action");
        log.info("для get запроса найден параметр action {}", action);
        processGetAction(req, resp, userId, action);
    }

    private void processGetAction(HttpServletRequest req, HttpServletResponse resp, String userId, String action) throws ServletException, IOException {
        log.info("Начинаю обработку действия ползователя: {}, выбор: {}", userId, action );
        if (action == null) {
            throw new IllegalArgumentException("action required");
        }

        if ("listSaves".equals(action)) {
            // Получаем список сохранений
            var saves = saveManager.listSaves(userId);
            req.setAttribute(AppConstant.RequestAttrNames.SAVES, saves);
            req.getRequestDispatcher(AppConstant.AppLinks.FILE_JSP_LIST_SAVES).forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }


    }

    private void processPostActionSave(HttpServletRequest req, HttpServletResponse resp, String username, String slotName) throws IOException {
        log.info("processPostActionSave user={} slot={}", username, slotName);
        Object sessionState = req.getSession().getAttribute(AppConstant.SessionKeys.SESSION_KEY_GAME_STATE);
        if(slotName == null){
            log.warn("slotName is null");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "slotName is NULL");
            return;
        }
        if(username == null){
            log.warn("username is null");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "username is NULL");
            return;
        }
        if(sessionState == null){
            log.warn("sessionState is null");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "sessionState is NULL");
            return;
        }


        if (! (sessionState instanceof GameState)){
            log.warn("sessionState is not instanceof GameState");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "sessionState is not instanceof GameState");
            return;
        }
        log.info("Found game state (obj)");
        GameState gameState = (GameState) sessionState;
        gameStateValidator.ensureSceneLoaded(gameState);

        saveManager.save(gameState, username, slotName);
        resp.sendRedirect(req.getContextPath() + "/game?action=show");
    }

    private void processPostActionLoad(HttpServletRequest req, HttpServletResponse resp, String username, String slotName) throws IOException, ServletException {
        log.info("processPostActionLoad user={} slot={}", username, slotName);
        if (slotName == null || username == null) {
            log.warn("username или slotName null");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "username or slotName null");
            return;
        }

        GameState loaded;
        try {
            loaded = saveManager.load(username, slotName);
        } catch (IOException e) {
            log.error("Ошибка при загрузке сохранения {}/{}", username, slotName, e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot load save: " + e.getMessage());
            return;
        }

        if (loaded == null) {
            log.warn("Save not found: {}/{}", username, slotName);
            resp.sendRedirect(req.getContextPath() + "/saves?action=listSaves");
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute(AppConstant.RequestAttrNames.GAME_STATE, loaded);
        session.setAttribute(AppConstant.RequestAttrNames.USER_ID, username);

        resp.sendRedirect(req.getContextPath() + "/game?action=show");
    }



    private void processPostActionRemove(HttpServletRequest req, HttpServletResponse resp,
                                         String username, String slotName) throws IOException {
        log.info("handleRemoveSave");
        saveManager.delete(username, slotName);
        resp.sendRedirect(req.getContextPath() + "/game?action=listSaves");

    }
}