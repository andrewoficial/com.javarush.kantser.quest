package ru.kantser.game.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.service.game.SaveGameManager;
import java.io.IOException;

@WebServlet("/saves")
public class ListSavesServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ListSavesServlet.class);
    private SaveGameManager saveManager;

    @Override
    public void init() throws ServletException {
        log.info("init");
        String savePath = getServletContext().getRealPath("/saves");
        this.saveManager = new SaveGameManager(savePath);
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
        String userId = (String) req.getSession().getAttribute("userId");
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
            req.setAttribute("saves", saves);
            req.getRequestDispatcher("/WEB-INF/JSP/list-saves.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неизвестное действие: " + action);
        }


    }

    private void processPostActionLoad(HttpServletRequest req, HttpServletResponse resp, String username, String slotName) throws IOException, ServletException {
        log.info("processPostActionLoad user={} slot={}", username, slotName);

        if (slotName == null || username == null) {
            log.warn("username или slotName null");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "username или slotName null");
            return;
        }

        // Загружаем сохранение (используем saveManager)
        ru.kantser.game.model.state.game.GameState loaded;
        try {
            loaded = saveManager.load(username, slotName);
        } catch (IOException e) {
            log.error("Ошибка при загрузке сохранения {}/{}", username, slotName, e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot load save: " + e.getMessage());
            return;
        }

        if (loaded == null) {
            log.warn("Save not found: {}/{}", username, slotName);
            // редиректим обратно к списку с сообщением (можно добавить param ?msg=notfound)
            resp.sendRedirect(req.getContextPath() + "/saves?action=listSaves");
            return;
        }

        // Кладём состояние в сессию и редиректим на /game?action=show (GET)
        HttpSession session = req.getSession();
        session.setAttribute("gameState", loaded);
        session.setAttribute("userId", username); // на всякий случай, если нет

        // Post-Redirect-Get: после POST делаем redirect на страницу игры (GET)
        resp.sendRedirect(req.getContextPath() + "/game?action=show");
    }

    private void processPostActionRemove(HttpServletRequest req, HttpServletResponse resp,
                                         String username, String slotName) throws IOException {
        log.info("handleRemoveSave");
        resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Удаление не реализовано.");

    }
}