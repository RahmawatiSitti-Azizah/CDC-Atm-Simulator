<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix= "c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link href='<c:url value="/bootstrap.css"/>' rel="stylesheet" type="text/css"/>
        <link href='<c:url value="/bootstrap.js"/>' rel="stylesheet" type="text/javascript"/>
    <title>Transfer Menu</title>
    </head>
    <body class="container vh-100">
      <div class="row justify-content-center align-items-center vh-100">
          <div class="col-3 bg-primary">
            <div class="row justify-content-center my-2">
                <div class="col-auto"><h3>Provide destination account details</h3></div>
            </div>
            <c:if test="${errorMessage != null}">
              <div class="row justify-content-center">
                <div class="col-auto"><label class="text-danger"><c:out value="${errorMessage}"/></label></div>
              </div>
            </c:if>
            <form action="/transfer" method="post">
                <div class="row justify-content-center my-2">
                    <div class="col-6 text-end">
                        <label for="accountNumber" class="form-label">Account number:</label>
                    </div>
                    <div class="col-6">
                        <input class="form-control" type="number"  name="destAccount" id="accountNumber">
                    </div>
                </div>
                <div class="row justify-content-center my-2">
                    <div class="col-6 text-end">
                        <label for="amount" class="form-label">Transfer amount:</label>
                    </div>
                    <div class="col-6">
                        <input class="form-control" type="number" name="amount" id="amount">
                    </div>
                </div>
                <div class="row my-2 justify-content-center">
                    <div class="col-5">
                        <input type="submit" class="form-control" value ="Transfer"/>
                    </div>
                    </form>
                    <div class="col-5">
                      <form action="/login" method= "post">
                        <input type="submit" class="form-control" value ="Menu"/>
                      </form>
                    </div>
                </div>
            </form>
          </div>
      </div>
    </body>
</html>