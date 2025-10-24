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
          <div class="row justify-content-center">
            <div class="col-auto">
              <h3><c:out value="Transaction Summary"/></h3>
              <h4><c:out value="Date : ${transactionDate}"/></h4>
              <h4><c:out value="Withdraw: ${withdraw}"/></h4>
              <h4><c:out value="Balance: ${balance}"/></h4>
            </div>
          </div>
          <div class="row justify-content-center">
            <div class="col-6">
              <form action="/login" method="post">
                <input type="submit" class="btn btn-primary" value="Menu"/>
              </form>
            </div>
            <div class="col-6">
              <form action="/logout">
                <input type="submit" class="btn btn-primary" value="Exit"/>
              </form>
            </div>
          </div>
        </div>
      </div>
    </body>
</html>