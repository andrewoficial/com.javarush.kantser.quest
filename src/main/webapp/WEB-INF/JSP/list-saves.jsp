<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Сохранения</title>
    <style>
        body {
            background-color: #e8f5e9;
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

        .menu a {
            color: #2e7d32;
            text-decoration: none;
            font-weight: bold;
            margin: 0 10px;
        }

        .menu a:hover {
            text-decoration: underline;
        }

        .saves-container {
            background-color: rgba(255, 255, 255, 0.9);
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .save-item {
            background-color: rgba(255, 255, 255, 0.8);
            padding: 15px;
            border-radius: 5px;
            margin: 10px 0;
            border-left: 4px solid rgba(76, 175, 80, 0.7);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .save-name {
            font-size: 18px;
            font-weight: bold;
            color: #2e7d32;
        }

        .save-actions {
            display: flex;
            gap: 10px;
        }

        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            font-weight: bold;
            transition: background-color 0.3s;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }

        .btn-load {
            background-color: rgba(76, 175, 80, 0.7);
            color: white;
        }

        .btn-load:hover {
            background-color: rgba(76, 175, 80, 0.9);
        }

        .btn-delete {
            background-color: rgba(244, 67, 54, 0.7);
            color: white;
        }

        .btn-delete:hover {
            background-color: rgba(244, 67, 54, 0.9);
        }

        .btn-back {
            background-color: rgba(33, 150, 243, 0.7);
            color: white;
            padding: 10px 20px;
            margin-top: 20px;
        }

        .btn-back:hover {
            background-color: rgba(33, 150, 243, 0.9);
        }

        .empty-state {
            text-align: center;
            padding: 40px;
            color: #666;
            font-style: italic;
        }

        h1 {
            color: #2e7d32;
            text-align: center;
            margin-bottom: 30px;
        }

        form {
            margin: 0;
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

    <div class="saves-container">
        <h1>Сохранения</h1>

        <c:if test="${empty saves}">
            <div class="empty-state">
                <p>У вас пока нет сохранений.</p>
            </div>
        </c:if>

        <c:forEach var="slot" items="${saves}">
            <div class="save-item">
                <span class="save-name">${slot}</span>
                <div class="save-actions">
                    <form method="post" action="saves" style="display:inline;">
                        <input type="hidden" name="username" value="${sessionScope.userId}">
                        <input type="hidden" name="slotName" value="${slot}">
                        <input type="hidden" name="action" value="load">
                        <button type="submit" class="btn btn-load">Загрузить</button>
                    </form>

                    <form method="post" action="saves" style="display:inline;">
                        <input type="hidden" name="username" value="${sessionScope.userId}">
                        <input type="hidden" name="slotName" value="${slot}">
                        <input type="hidden" name="action" value="remove">
                        <button type="submit" class="btn btn-delete">Удалить</button>
                    </form>
                </div>
            </div>
        </c:forEach>

        <div style="text-align: center; margin-top: 30px;">
            <a href="game" class="btn btn-back">Назад в игру</a>
        </div>
    </div>
</body>
</html>