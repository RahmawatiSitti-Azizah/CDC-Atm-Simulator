<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix= "c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
  <form action="login" method="post">
    <h3>Account number:<input type="text" name="username"/></h3>
    <h3>Pin:<input type="password" name ="pin"/></h3>
    <input type="submit" value ="ok"/>
  </form>
</body>
</html>