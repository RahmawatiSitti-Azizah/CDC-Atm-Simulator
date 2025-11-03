<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date,java.text.SimpleDateFormat"%>
<%@ taglib prefix= "c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <link href='<c:url value="bootstrap.css"/>' rel="stylesheet" type="text/css"/>
        <link href='<c:url value="bootstrap.js"/>' rel="stylesheet" type="text/javascript"/>
        <meta charset="UTF-8">
        <title>History List</title>
    </head>
    <body class="container vh-100">
      <div class="row justify-content-center align-items-center vh-100">
        <div class="col-4 bg-primary">
          <c:if test="${transaction.isEmpty()}">
            <div class="row justify-content-center">
              <div class="col-auto"><c:out value = "No Transaction Found"/></div>
            </div>
          </c:if>
          <c:forEach var="trx" items="${transaction}">
            <div class="row">
              <c:choose>
                <c:when test="${trx.sourceAccountNumber == account.accountNumber}">
                    <div class="col-auto fw-bold text-warning">
                      <c:out value ="${trx}"/>
                    </div>
                </c:when>
                 <c:otherwise>
                    <div class="col-auto fw-bold text-white">
                      <c:out value ="${trx.printIncomingTransaction()}"/>
                    </div>
                 </c:otherwise>
              </c:choose>
            </div>
          </c:forEach>
          <div class="row justify-content-center">
            <div class="col-6">
              <form action="/transaction">
                <input type="submit" class="btn btn-primary" value="Back"/>
              </form>
            </div>
            <div class="col-6 text-end">
              <form action="/login" method="post">
                <input type="submit" class="btn btn-primary" value="Main Menu"/>
              </form>
            </div>
          </div>
        </div>
      </div>
    </body>
</html>