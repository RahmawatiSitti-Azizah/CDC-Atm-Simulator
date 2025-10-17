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
        <div class="col-auto"><label>How many latest transaction</label></div>
      </div>
      <div class="row justify-content-center">
        <div class="col-6">
          <form action="/transaction" method="post">
            <input type="hidden" name="max" value="10"/>
            <input type="submit" class="form-control btn btn-primary" value="10"/>
          </form>
        </div>
        <div class="col-6">
          <form action="/transaction" method="post">
            <input type="hidden" name="max" value="25"/>
            <input type="submit" class="form-control btn btn-primary" value="25"/>
          </form>
        </div>
        <div class="col-6">
          <form action="/transaction" method="post">
            <input type="hidden" name="max" value="50"/>
            <input type="submit" class="form-control btn btn-primary" value="50"/>
          </form>
        </div>
        <div class="col-6">
          <form action="/transaction" method="post">
            <input type="hidden" name="max" value="100"/>
            <input type="submit" class="form-control btn btn-primary" value="100"/>
          </form>
        </div>
      </div>
    </div>
  </div>
</body>
</html>