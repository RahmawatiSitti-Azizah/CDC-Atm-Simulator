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
      <form action="/transfer" method="post" class="row justify-content-center align-items-center vh-100">
          <div class="col-3 bg-primary">
            <c:if test="${errorMessage != null}">
              <div class="row justify-content-center">
                <div class="col-auto"><label><c:out value="${error}"/></label></div>
              </div>
            </c:if>
            <div class="row justify-content-center my-2">
                <div class="col-auto"><h3>Provide destination account details</h3></div>
            </div>
            <div class="row justify-content-center my-2">
                <div class="col-4">
                    <label for="accountNumber" class="form-label">Account number:</label>
                </div>
                <div class="col-8">
                    <input class="form-control" type="number"  name="destAccount" id="accountNumber">
                </div>
            </div>
            <div class="row justify-content-center my-2">
                <div class="col-4">
                    <label for="amount" class="form-label">Transfer amount</label>
                </div>
                <div class="col-8">
                    <input class="form-control" type="number"  name="amount" id="accountNumber">
                </div>
            </div>
            <div class="row my-2 justify-content-end">
                <div class="col-auto">
                  <form action="/transfer">
                    <input type="submit" class="form-control" value ="transfer"/>
                  </form>
                </div>
            </div>
          </div>
      </form>
    </body>
</html>