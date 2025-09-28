<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userId = (String) session.getAttribute("userId");
    if (userId != null) {
        response.sendRedirect("game");
        return;
    }
%>
<html>
<head>
    <title>My game</title>
    <style>
        body {
            background-color: #e8f5e9; /* Светло-зеленый фон */
            font-family: Arial, sans-serif;
            margin: 20px;
            line-height: 1.6;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 80vh;
        }

        .welcome-container {
            background-color: rgba(255, 255, 255, 0.9);
            padding: 30px;
            border-radius: 8px;
            margin: 20px 0;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            max-width: 600px;
            text-align: center;
        }

        .project-info {
            background-color: rgba(255, 248, 225, 0.8);
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
            border-left: 4px solid #ff9800;
        }

        .start-button {
            background-color: rgba(76, 175, 80, 0.7);
            border: none;
            border-radius: 5px;
            padding: 15px 30px;
            margin: 20px 0;
            text-align: center;
            cursor: pointer;
            transition: background-color 0.3s;
            font-size: 18px;
            color: white;
            font-weight: bold;
            text-decoration: none;
            display: inline-block;
        }

        .start-button:hover {
            background-color: rgba(76, 175, 80, 0.9);
            text-decoration: none;
            color: white;
        }

        h1 {
            color: #2e7d32;
            margin-bottom: 30px;
        }

        h2 {
            color: #388e3c;
            margin-bottom: 15px;
        }

        .game-description {
            font-size: 16px;
            line-height: 1.8;
            margin: 15px 0;
        }
    </style>
</head>
<body>
    <div class="welcome-container">
        <h1>Добро пожаловать!</h1>

        <div class="project-info">
            <h2>Итоговый проект по модулю 3</h2>
            <p><strong>Автор:</strong> Канцер Андрей</p>
            <p><strong>Игра:</strong> "Прокрастинация"</p>
        </div>

        <div class="game-description">
            <h3>Цель игры:</h3>
            <p>Соблюсти баланс между продуктивностью и выгоранием.</p>
            <p>Принимайте мудрые решения, управляйте своим временем и энергией,
            чтобы достигать целей, не теряя мотивацию и здоровье.</p>
        </div>

        <a href="login.html" class="start-button">Войти / Регистрация</a>
    </div>
</body>
</html>