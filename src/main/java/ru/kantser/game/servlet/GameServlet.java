package ru.kantser.game.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.builder.scene.SceneJsonBuilder;
import ru.kantser.game.model.scene.GameScene;
import ru.kantser.game.model.scene.Scene;
import ru.kantser.game.model.scene.choice.Choice;
import ru.kantser.game.model.state.game.GameState;
import ru.kantser.game.model.state.player.PlayerState;
import ru.kantser.game.model.state.scene.SceneState;
import ru.kantser.game.service.game.GameManager;
import ru.kantser.game.service.game.SaveGameManager;

import java.io.IOException;
import java.util.*;

@WebServlet("/game")
public class GameServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(GameServlet.class);
    private static final String SESSION_KEY_GAME_STATE = "gameState";
    private static final String SESSION_KEY_PENDING_LOAD_USER = "pendingLoadUser";
    private static final String SESSION_KEY_PENDING_LOAD_SLOT = "pendingLoadSlot";

    private GameManager gameManager;
    private SaveGameManager saveManager;

    @Override
    public void init() throws ServletException {
        log.info("init");
        String savePath = getServletContext().getRealPath("/saves");
        String scenesPath = getServletContext().getRealPath("/WEB-INF/scenes");
        this.saveManager = new SaveGameManager(savePath);
        this.gameManager = new GameManager(scenesPath);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.info("doGet" );
        String userId = (String) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.html");
            return;
        }

        String action = req.getParameter("action");
        if(action == null){
            log.warn("Пустой action");
        }
        try {
            switch (action != null ? action : "showCurrentGame") { // Default to show
                case "load":
                    loadGame(req, resp, userId);
                    break;
                case "show":
                    showCurrentGame(req, resp, userId);
                    break;
                case "listSaves":
                    listSaves(req, resp, userId);
                    break;
                case "new":
                    newGame(req, resp, userId);
                    break;
                case "choice":
                    String choiceId = req.getParameter("choiceId");
                    processChoice(req, resp, userId, choiceId);
                    break;
                default:
                    log.warn("Загружаю default-case (текущую игру), потому что action {}, ", action);
                    showCurrentGame(req, resp, userId);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.info("doPost" );
        String userId = (String) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.html");
            return;
        }

        String actionType = req.getParameter("actionType");
        String choiceId = req.getParameter("choiceId");

        if ("choice".equals(actionType)) {
            handleChoicePost(req, resp, userId, choiceId);
        } else if ("serverCommand".equals(actionType)) {
            handleServerCommandPost(req, resp, userId, req.getParameter("choiceId"));
        } else {
            handleChoicePost(req, resp, userId, choiceId);
        }
    }

    private void handleChoicePost(HttpServletRequest req, HttpServletResponse resp, String userId, String choiceId) throws IOException {
        log.info("handleChoicePost user={} choice={}", userId, choiceId);
        if (choiceId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "choiceId required");
            return;
        }

        GameState currentState = saveManager.load(userId, "auto");
        if (currentState == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No current game");
            return;
        }

        GameState updatedState = gameManager.processChoice(currentState, choiceId);
        saveManager.save(updatedState, userId, "auto");

        req.getSession().setAttribute(SESSION_KEY_GAME_STATE, updatedState);

        resp.sendRedirect(req.getContextPath() + "/game?action=show");
    }

    private void handleServerCommandPost(HttpServletRequest req, HttpServletResponse resp, String userId, String command) throws IOException {
        log.info("handleServerCommandPost user={} command={}", userId, command);

        String username = req.getParameter("username");
        String slotName = req.getParameter("slotName");
        if (username != null && slotName != null) {
            req.getSession().setAttribute(SESSION_KEY_PENDING_LOAD_USER, username);
            req.getSession().setAttribute(SESSION_KEY_PENDING_LOAD_SLOT, slotName);
        }
        resp.sendRedirect(req.getContextPath() + "/game?action=show");
    }

    private void loadGame(HttpServletRequest req, HttpServletResponse resp, String userId) throws IOException, ServletException {

        String username = req.getParameter("username");
        String slotName = req.getParameter("slotName");

        if (username == null || slotName == null) {
            throw new IllegalArgumentException("saveId required");
        }
        GameState gameState = saveManager.load(username, slotName);
        req.setAttribute("gameState", gameState);
        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    private void listSaves(HttpServletRequest req, HttpServletResponse resp, String userId) throws ServletException, IOException {
        log.info("Вывожу список сохранений");
        List<String> saves = saveManager.listSaves(userId);
        req.setAttribute("saves", saves);
        log.info("Перенаправляю на saves");
        req.getRequestDispatcher("/saves").forward(req, resp); // Или game с <c:choose>
    }

    private void newGame(HttpServletRequest req, HttpServletResponse resp, String userId) throws IOException, ServletException {
        log.info("Run create new game for user: {}", userId );

        GameState newState = new GameState();
        log.info("Create gameState");

        Map<String, Integer> resources = new HashMap<>();
        Map<String, Integer> skills = new HashMap<>();
        Set<String> inventory = new HashSet<>();

        PlayerState playerState = new PlayerState(userId, resources, skills, inventory, "start", "morning", 85, 1);
        newState.setPlayerState(playerState);
        log.info("Create playerState and put it in gameState");


        GameScene firstScene = gameManager.getSceneBuilder().getScene("start");
        SceneState sceneState = new SceneState();
        sceneState.setCurrentSceneId("start");
        sceneState.setCurrentScene(firstScene);

        newState.setSceneState(sceneState);
        log.info("Create sceneState and put it in gameState");

        // Auto-save
        saveManager.save(newState, userId, "auto");
        log.info("Вызвал автосохранение");

        req.setAttribute("gameState", newState);
        log.info("Атрибут запроса gameState установил как {}", newState);
        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    private void showCurrentGame(HttpServletRequest req, HttpServletResponse resp, String userId) throws IOException, ServletException {
        log.info("showCurrentGame");
        // 1) Если в сессии есть явно переданный gameState — используем его
        Object sessionState = req.getSession().getAttribute(SESSION_KEY_GAME_STATE);
        GameState gameState = null;

        // 2) Если есть pending load (от ListSavesServlet или handleServerCommandPost) — применяем его
        String pendingUser = (String) req.getSession().getAttribute(SESSION_KEY_PENDING_LOAD_USER);
        String pendingSlot = (String) req.getSession().getAttribute(SESSION_KEY_PENDING_LOAD_SLOT);
        log.info("pendingUser {}, pendingSlot {}", pendingUser, pendingSlot);
        if (pendingUser != null && pendingSlot != null) { //Если данные для сохранения передаются полями запроса
            log.info("Found pending load: {}/{}", pendingUser, pendingSlot);
            gameState = saveManager.load(pendingUser, pendingSlot);
            log.info(gameState.getSceneState().getCurrentScene().getChoices().toString());
            log.info(gameState.getSceneState().getCurrentScene().getTitle());


            req.getSession().removeAttribute(SESSION_KEY_PENDING_LOAD_USER);
            req.getSession().removeAttribute(SESSION_KEY_PENDING_LOAD_SLOT);

            req.getSession().setAttribute("userId", pendingUser);
            req.getSession().setAttribute(SESSION_KEY_GAME_STATE, gameState);
        } else if (sessionState instanceof GameState) { //Если передаётся целиком состояние игры
            log.info("Найдено состояние игры ");
            gameState = (GameState) sessionState;
            ensureSceneLoaded(gameState);
            req.setAttribute("gameState", gameState);
            req.getRequestDispatcher("/game.jsp").forward(req, resp);
        } else {
            log.warn("pendingUser or pendingSlot is NULL  фтd received not instance of GameState, load last autosave");
            if(saveManager.exists(userId, "auto")){
                // 3) По умолчанию — загружаем auto-save
                gameState = saveManager.load(userId, "auto");
                if (gameState == null) {
                    log.warn("Can not load autosave, run new game (by redirect)");
                    // нет autosave — создаём новую игру
                    resp.sendRedirect(req.getContextPath() + "/game?action=new");
                    return;
                }else{
                    ensureSceneLoaded(gameState);
                    req.setAttribute("gameState", gameState);
                    req.getRequestDispatcher("/game.jsp").forward(req, resp);
                }
            }else{
                log.warn("Autosave not found. Run new game (by redirect)");
                // нет autosave — создаём новую игру
                resp.sendRedirect(req.getContextPath() + "/game?action=new");
            }

        }

    }


    private void processChoice(HttpServletRequest req, HttpServletResponse resp, String userId, String choiceId) throws IOException, ServletException {
        log.info("Начинаю обработку выбора ползователя: {}, выбор: {}", userId, choiceId );
        if (choiceId == null) {
            throw new IllegalArgumentException("choiceId required");
        }
        // Загружаем текущий state (auto)
        GameState currentState = saveManager.load(userId, "auto");
        if (currentState == null) {
            throw new IllegalStateException("No current game");
        }

        // Process
        GameState updatedState = gameManager.processChoice(currentState, choiceId);

        // Auto-save
        ensureSceneLoaded(updatedState);
        saveManager.save(updatedState, userId, "auto");
        req.setAttribute("gameState", updatedState);
        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    private void ensureSceneLoaded(GameState gameState) {
        if (gameState == null) return;
        if (gameState.getSceneState() == null) return;

        // предполагаем, что SceneState имеет поле currentScene (тип Scene) и currentSceneId (String)
        SceneState sceneState = gameState.getSceneState();
        // если currentScene уже заполнен — ничего делать не надо
        if (sceneState.getCurrentScene() != null) return;

        String sceneId = sceneState.getCurrentSceneId();
        if (sceneId == null) return;

        try {
            GameScene scene = gameManager.getSceneBuilder().getScene(sceneId);
            sceneState.setCurrentScene(scene);
        } catch (Exception e) {
            log.warn("Не удалось загрузить сцену {}: {}", sceneId, e.getMessage());
        }
    }
}