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
</head>
<body>
    <h1>Welcome!</h1>
    <a href="login.html">Войти / Регистрация</a>
</body>
</html>