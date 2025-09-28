<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Game</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/game.css">
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
            <p>День: ${gameState.playerState.day} | Время: ${gameState.playerState.timeSlot} |
               Силы: ${gameState.playerState.energy} | Свободное время: ${gameState.playerState.freeMinutes}</p>
            <p>Инвентарь:
                <c:forEach var="resource" items="${gameState.playerState.resources}" varStatus="status">
                    ${resource.key}: ${resource.value}<c:if test="${!status.last}">, </c:if>
                </c:forEach>
            </p>
        </div>
    </c:if>

    <!-- Подсказка -->
    <c:if test="${not empty gameState.sceneState.tipText}">
        <div class="tip-text">${gameState.sceneState.tipText}</div>
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
<script>
function showSaveDialog() {
    document.getElementById('saveSlotName').value = '';
    document.getElementById('saveModal').style.display = 'block';
    document.getElementById('saveSlotName').focus();
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