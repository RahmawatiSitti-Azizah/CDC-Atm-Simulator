<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date,java.text.SimpleDateFormat"%>
<%@ taglib prefix= "c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <link href='<c:url value="bootstrap.css"/>' rel="stylesheet" type="text/css"/>
        <link href='<c:url value="bootstrap.js"/>' rel="stylesheet" type="text/javascript"/>
        <meta charset="UTF-8">
        <title>Withdraw Summary</title>
    </head>
    <body class="container vh-100">
      <div class="row justify-content-center align-items-center vh-100">
        <div class="col-4 bg-primary">
          <c:forEach var="trx" items="${transaction}">
            <div class="row justify-content-center">
                <div class="col-auto">
                  <c:out value ="${trx}"/>
                </div>
            </div>
          </c:forEach>
          <div class="row justify-content-center">
            <div class="col-6">
              <form action="/transaction">
                <input type="submit" class="btn btn-primary" value="Back"/>
              </form>
            </div>
            <div class="col-6">
              <form action="/login" method="post">
                <input type="submit" class="btn btn-primary" value="Main Menu"/>
              </form>
            </div>
          </div>
        </div>
      </div>
    </body>
</html>