<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Сохранения</title>
</head>
<body>
    <h1>Сохранения</h1>

    <c:if test="${empty saves}">
        <p>У вас пока нет сохранений.</p>
    </c:if>

    <c:forEach var="slot" items="${saves}">
        <div>
            <form method="post" action="saves" style="display:inline;">
                <input type="hidden" name="username" value="${sessionScope.userId}">
                <input type="hidden" name="slotName" value="${slot}">
                <input type="hidden" name="action" value="load">
                <button type="submit">Загрузить ${slot}</button>
            </form>

            <form method="post" action="saves" style="display:inline;">
                <input type="hidden" name="username" value="${sessionScope.userId}">
                <input type="hidden" name="slotName" value="${slot}">
                <input type="hidden" name="action" value="remove">
                <button type="submit">Удалить</button>
            </form>
        </div>
    </c:forEach>

    <p><a href="game">Назад в игру</a></p>
</body>
</html>
