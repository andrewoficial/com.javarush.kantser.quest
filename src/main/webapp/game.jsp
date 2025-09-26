<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Game</title>
    <style>
        body {
            background-color: #e8f5e9; /* Светло-зеленый фон */
            font-family: Arial, sans-serif;
            margin: 20px;
            line-height: 1.6;
        }

        .menu {
            background-color: rgba(255, 255, 255, 0.9);
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .scene-container {
            background-color: rgba(255, 255, 255, 0.8);
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
        }

        .choice-button {
            background-color: rgba(76, 175, 80, 0.7); /* Полупрозрачный зеленый */
            border: none;
            border-radius: 5px;
            padding: 12px 20px;
            margin: 5px 0;
            width: 100%;
            text-align: left;
            cursor: pointer;
            transition: background-color 0.3s;
            font-size: 16px;
        }

        .choice-button:hover {
            background-color: rgba(76, 175, 80, 0.9);
        }

        .choice-form {
            display: block;
            margin: 10px 0;
        }

        .stats {
            background-color: rgba(255, 255, 255, 0.9);
            padding: 10px;
            border-radius: 5px;
            display: inline-block;
            margin: 10px 0;
        }

        .choices-list {
            list-style-type: none;
            padding-left: 0;
        }
    </style>
</head>
<body>
    <!-- Меню вверху страницы -->
    <div class="menu">
        <a href="game?action=listSaves">Список сохранений</a> |
        <a href="game?action=new">Новая игра</a> |
        <a href="logout">Выход</a>
    </div>

    <h1>${gameState.sceneState.currentScene.title}</h1>

    <!-- Блок с игровой статистикой -->
    <c:if test="${not empty gameState.playerState}">
        <div class="stats">
            <p>День: ${gameState.playerState.day} | Время: ${gameState.playerState.timeSlot} | Силы: ${gameState.playerState.energy} | Свободное время: ${gameState.playerState.freeMinutes}</p>
            <p>Инвентарь: ${gameState.playerState.resources}</p>
        </div>
    </c:if>

    <!-- Блок с текущей сценой -->
    <c:if test="${not empty gameState.sceneState.currentScene}">
        <div class="scene-container">
            <h2>${gameState.sceneState.currentScene.title}</h2>
            <p>${gameState.sceneState.currentScene.text}</p>

            <h3>Выборы:</h3>
            <ul class="choices-list">
                <c:forEach var="entry" items="${gameState.sceneState.currentScene.choices}">
                    <li>
                        <form method="post" action="${pageContext.request.contextPath}/game" class="choice-form">
                            <input type="hidden" name="actionType" value="choice"/>
                            <input type="hidden" name="choiceId" value="${entry.key}"/>
                            <button type="submit" class="choice-button">
                                <strong>${entry.key}.</strong> ${entry.value.description}
                            </button>
                        </form>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
</body>
</html>