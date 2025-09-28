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


//@WebServlet("/")
//public class StartPageServlet extends HttpServlet {
//    private static final Logger log = LoggerFactory.getLogger(StartPageServlet.class);
//
//    @Override
//    public void init() throws ServletException {
//        log.info("init");
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
//        log.info("doPost");
//        //resp.sendRedirect(req.getContextPath() + "/login.html");
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
//        //resp.sendRedirect(req.getContextPath() + "/login.html");
//    }
//
//}

