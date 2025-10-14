<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix= "c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <link href='<c:url value="bootstrap.css"/>' rel="stylesheet" type="text/css"/>
    <link href='<c:url value="bootstrap.js"/>' rel="stylesheet" type="text/javascript"/>
    <meta charset="UTF-8">
    <title>Menu</title>
</head>
<body class="container vh-100">
  <div class="row justify-content-center align-items-center vh-100">
    <div class="col-4 bg-primary">
      <div class="row justify-content-center">
        <div class="col-auto">Hello <c:out value = "${account.accountHolderName}"/></div>
      </div>
      <div class="row justify-content-center">
        <div class="col-6"><form action="/withdraw"><input type="submit" class="form-control btn btn-primary" value="Withdrawn"/></form></div>
        <div class="col-6"><form action="/transfer"><input type="submit" class="form-control btn btn-primary" value="Fund Transfer"/></form></div>
        <div class="col-6"><form action="/transaction"><input type="submit" class="form-control btn btn-primary" value="Transaction History"/></form></div>
        <div class="col-6"><form action="/logout"><input type="submit" class="form-control btn btn-primary" value="Exit"/></form></div>
      </div>
    </div>
  </div>
</body>
</html>