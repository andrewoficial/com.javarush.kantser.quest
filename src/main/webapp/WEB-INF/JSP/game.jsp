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
        /* Стили для модального окна */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
        }

        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border-radius: 8px;
            width: 300px;
            text-align: center;
        }

        .modal-input {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }

        .modal-buttons {
            margin-top: 15px;
        }

        .btn-modal {
            padding: 8px 16px;
            margin: 0 5px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .btn-confirm {
            background-color: #4CAF50;
            color: white;
        }

        .btn-cancel {
            background-color: #f44336;
            color: white;
        }
    </style>
</head>
<body>
    <!-- Меню вверху страницы -->
    <div class="menu">
        <a href="game?action=listSaves">Список сохранений</a> |
        <a href="game?action=new">Новая игра</a> |
        <a href="#" onclick="showSaveDialog()">Сохранить</a> |
        <a href="logout">Выход</a>
    </div>
    <div id="saveModal" class="modal">
        <div class="modal-content">
            <h3>Сохранение игры</h3>
            <input type="text" id="saveSlotName" class="modal-input"
                   placeholder="Введите название сохранения" maxlength="50">
            <div class="modal-buttons">
                <button onclick="confirmSave()" class="btn-modal btn-confirm">Сохранить</button>
                <button onclick="closeSaveModal()" class="btn-modal btn-cancel">Отмена</button>
            </div>
        </div>
    </div>
    <h1>${gameState.sceneState.currentScene.title}</h1>

    <!-- Блок с игровой статистикой -->
    <c:if test="${not empty gameState.playerState}">
        <div class="stats">
            <p>День: ${gameState.playerState.day} | Время: ${gameState.playerState.timeSlot} | Силы: ${gameState.playerState.energy} | Свободное время: ${gameState.playerState.freeMinutes}</p>
            <p>Инвентарь: ${gameState.playerState.resources}</p>
        </div>
    </c:if>
    <i>${gameState.sceneState.getTipText()}</i>
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
<script>
function showSaveDialog() {
    document.getElementById('saveSlotName').value = '';
    document.getElementById('saveModal').style.display = 'block';
}

function closeSaveModal() {
    document.getElementById('saveModal').style.display = 'none';
}

function confirmSave() {
    const slotName = document.getElementById('saveSlotName').value.trim();
    if (slotName === '') {
        alert('Пожалуйста, введите название сохранения');
        return;
    }
    closeSaveModal();
    saveGame(slotName);
}

// Закрытие модального окна при клике вне его
window.onclick = function(event) {
    const modal = document.getElementById('saveModal');
    if (event.target === modal) {
        closeSaveModal();
    }
}

// Закрытие по ESC
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        closeSaveModal();
    }
});

function saveGame(slotName) {
    // Отправка формы (как в предыдущем примере)
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = 'saves';

    const usernameInput = document.createElement('input');
    usernameInput.type = 'hidden';
    usernameInput.name = 'username';
    usernameInput.value = '${sessionScope.userId}';

    const slotNameInput = document.createElement('input');
    slotNameInput.type = 'hidden';
    slotNameInput.name = 'slotName';
    slotNameInput.value = slotName;

    const actionInput = document.createElement('input');
    actionInput.type = 'hidden';
    actionInput.name = 'action';
    actionInput.value = 'save';

    form.appendChild(usernameInput);
    form.appendChild(slotNameInput);
    form.appendChild(actionInput);

    document.body.appendChild(form);
    form.submit();
}
</script>
</html>