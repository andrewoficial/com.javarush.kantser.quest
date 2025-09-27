package ru.kantser.game.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.exception.UserAlreadyExistsException;
import ru.kantser.game.service.auth.AuthService;
import ru.kantser.game.service.auth.SimpleAuthService;
import ru.kantser.game.service.game.SaveGameManager;

import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(AuthServlet.class);
    private AuthService authService;
    private SaveGameManager saveManager;

    @Override
    public void init() throws ServletException {
        log.info("init");
        String savePath = getServletContext().getRealPath("/saves");
        this.authService = new SimpleAuthService();
        this.saveManager = new SaveGameManager(savePath);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.info("doPost");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String action = req.getParameter("action");

        if (username == null || password == null || action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            if ("login".equals(action)) {
                handleLogin(req, resp, username, password);
            } else if ("register".equals(action)) {
                handleRegister(req, resp, username, password);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp, String username, String password) throws IOException, ServletException {
        log.info("handleRegister");
        try {
            authService.createUser(username, password); // Хранит plain password — в проде hash!
            req.setAttribute("message", "User created successfully. Please login.");
            req.getRequestDispatcher("login.html").forward(req, resp);
        } catch (UserAlreadyExistsException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("login.html").forward(req, resp);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp,
                             String username, String password) throws IOException {
        log.info("handleLogin");
        if (authService.authenticate(username, password)) {
            HttpSession session = req.getSession();
            session.setAttribute("userId", username);
            session.setMaxInactiveInterval(30 * 60); // 30 минут

            resp.sendRedirect("game?action=listSaves");
        } else {
            try {
                req.setAttribute("error", "Invalid credentials");
                req.getRequestDispatcher("login.html").forward(req, resp);
            } catch (ServletException e) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
            }
        }
    }
}