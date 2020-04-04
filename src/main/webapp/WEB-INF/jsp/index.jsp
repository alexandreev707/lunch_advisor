<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="msg.main.title"/></title>
    <jsp:include page="scripts.jsp"/>
</head>
<jsp:useBean id="menu" type="java.util.List<ru.lunch.advisor.web.response.MenuMainView>" scope="request"/>
<body>
<script type="text/javascript" src="resources/js/index.js" defer></script>
<nav class="navbar navbar-expand-md navbar-dark bg-dark justify-content-end">
    <button class="btn btn-primary mr-1" type="submit" id="registerId">
        <spring:message code="msg.btn.register"/>
        <i class="fa fa-sign-in"></i>
    </button>
    <button class="btn btn-success mr-1" type="submit" id="loginId">
        <spring:message code="msg.btn.login"/>
        <i class="fa fa-sign-in"></i>
    </button>
</nav>
<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="msg.index.title"/></h3>
        <div class="card-columns">
            <c:forEach var="m" items="${menu}">
                <div class="card mb-4 box-shadow">
                    <div class="card-header">
                        <h4 class="my-0 font-weight-normal">${m.restaurant}</h4>
                    </div>
                    <div class="card-body">
                        <h1 class="card-title pricing-card-title">${m.menu}</h1>
                        <h3 class="card-title pricing-card-title"><spring:message
                                code="msg.date.title"/>: ${m.date}</h3>
                        <ul class="list-unstyled mt-3 mb-4">
                            <c:forEach var="i" items="${m.items}">
                                <li>${i.name}</li>
                            </c:forEach>
                        </ul>
                        <br>
                        <h3 class="card-title pricing-card-title"><spring:message
                                code="msg.vote.title"/>: ${m.countVote}</h3>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
<!-- The modal login-->
<div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header border-bottom-0">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="form-title text-center">
                    <h4><spring:message code="msg.login.form.title"/></h4>
                </div>
                <div class="d-flex flex-column text-center">
                    <form id="login_form" action="spring_security_check" method="post">
                        <input class="form-control mr-1" type="text"
                               placeholder="<spring:message code="msg.email.placeholder"/>" name="username">
                        <br>
                        <input class="form-control mr-1" type="password"
                               placeholder="<spring:message code="msg.password.placeholder"/>" name="password">
                        <br>
                        <button class="btn btn-success" type="submit">
                            <spring:message code="msg.login"/>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- The modal register-->
<div class="modal fade" id="registrationModalId" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header border-bottom-0">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="form-title text-center">
                    <h4><spring:message code="msg.registration.form.title"/></h4>
                </div>
                <div class="d-flex flex-column text-center">
                    <form id="registrationForm">
                        <input class="form-control mr-1" type="text"
                               placeholder="<spring:message code="msg.username.placeholder"/>" name="name">
                        <br>
                        <input class="form-control mr-1" type="email"
                               placeholder="<spring:message code="msg.email.placeholder"/>" name="email">
                        <br>
                        <input class="form-control mr-1" type="password"
                               placeholder="<spring:message code="msg.password.placeholder"/>" id="pwd">
                        <br>
                    </form>
                </div>
                <!-- Modal footer -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal" id="saveId"><spring:message
                            code="msg.btn.save"/></button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal"><spring:message
                            code="msg.btn.close"/></button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
