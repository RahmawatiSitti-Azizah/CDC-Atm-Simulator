<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix= "c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link href='<c:url value="bootstrap.css"/>' rel="stylesheet" type="text/css"/>
        <link href='<c:url value="bootstrap.js"/>' rel="stylesheet" type="text/javascript"/>
    <title>Login</title>
    </head>
    <body class="container vh-100">
      <form action="login" method="post" class="row justify-content-center align-items-center vh-100">
          <div class="col-3 bg-primary">
            <div class="row justify-content-center my-2">
                <div class="col-auto"><h3>ATM SIMULATOR</h3></div>
            </div>
            <c:if test="${errorMessage != null}">
              <div class="row justify-content-center">
                <div class="col-auto"><label><c:out value="${errorMessage}"/></label></div>
              </div>
            </c:if>
            <div class="row justify-content-center my-2">
                <div class="col-4">
                    <label>Account:</label>
                </div>
                <div class="col-8">
                    <input class="form-control" type="text"  name="account">
                </div>
            </div>
            <div class="row justify-content-center my-2">
                <div class="col-4">
                    <label>Pin:</label>
                </div>
                <div class="col-8">
                    <input class="form-control" type="password" name ="pin"/>
                </div>
            </div>
            <div class="row my-2 justify-content-end">
                <div class="col-auto">
                    <input type="submit" class="form-control" value ="login"/>
                </div>
            </div>
          </div>
      </form>
    </body>
</html>