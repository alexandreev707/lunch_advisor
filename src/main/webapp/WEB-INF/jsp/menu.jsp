<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="msg.menu.title"/></title>
    <jsp:include page="scripts.jsp"/>
</head>
<jsp:useBean id="restaurants" type="java.util.List<ru.lunch.advisor.persistence.model.RestaurantModel>" scope="request"/>
<body>
<script type="text/javascript" src="resources/js/menu.js" defer></script>
<script type="text/javascript" src="resources/js/common.js" defer></script>
<nav class="navbar navbar-expand-md navbar-dark bg-dark justify-content-center">
    <ul class="navbar-nav mx-auto text-center">
        <li class="nav-item">
            <a class="nav-link active" href="admin"><spring:message code="msg.restaurant"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="users"><spring:message code="msg.user"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="javascript:history.back()"><spring:message code="msg.btn.back"/></a>
        </li>
    </ul>
    <a class="btn btn-primary" href="logout">
        <spring:message code="msg.logout"/>
        <i class="fa fa-sign-out"></i>
    </a>
</nav>
<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="msg.menu.title"/></h3>
        <div class="card border-dark">
            <div class="card-body pb-0">
                <form id="filter">
                    <div class="row">
                        <div class="col-3">
                            <label for="startDate"><spring:message code="msg.menu.start.date.filter"/></label>
                            <input class="form-control" name="startDate" type="date" id="startDate" autocomplete="off">
                        </div>
                        <div class="col-3">
                            <label for="endDate"><spring:message code="msg.menu.end.date.filter"/></label>
                            <input class="form-control" type="date" name="endDate" id="endDate" autocomplete="off">
                        </div>
                        <div class="col-3">
                            <label for="endDate"><spring:message code="msg.menu.restaurant.filter"/></label>
                            <select class="custom-select" name="restaurantModel" id="restaurantSelectId">
                                <option datatype="text" name="restaurantModel"></option>
                                <c:forEach var="r" items="${restaurants}">
                                    <option datatype="text" name="restaurantModel" value="${r.name}">${r.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="card-footer text-right">
                <button class="btn btn-danger" onclick="clearFilter()">
                    <span class="fa fa-remove"></span>
                    <spring:message code="msg.btn.clean"/>
                </button>
                <button class="btn btn-primary" onclick="filter()">
                    <span class="fa fa-filter"></span>
                    <spring:message code="msg.btn.filter"/>
                </button>
            </div>
        </div>
        <br>
        <table class="table table-striped table-hover" id="datatable">
            <thead>
            <th><spring:message code="msg.menu.head.table.restaurant"/></th>
            <th><spring:message code="msg.menu.head.table.menu"/></th>
            <th><spring:message code="msg.menu.head.table.date"/></th>
            <th/>
            <th/>
            </thead>
        </table>
        <br>
        <button type="button" class="btn btn-primary" id="createId" onclick="create()">
            <span class="fa fa-plus"></span>
            <spring:message code="msg.btn.add"/>
        </button>
    </div>
</div>
<!-- The Modal -->
<div class="modal fade" id="mainForm">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title"><spring:message code="msg.menu.edit.form.title"/></h4>
            </div>
            <!-- Modal body -->
            <div class="modal-body">
                <form action="" id="modalForm">
                    <input type="hidden" id="menuId" value="">
                    <div class="form-group">
                        <label for="menu" class="col-form-label"><spring:message
                                code="msg.menu.modal.form.menu"/></label>
                        <input type="text" id="menu" value=""/>
                    </div>
                    <div class="form-group">
                        <label for="date" class="col-form-label"><spring:message
                                code="msg.menu.modal.form.date"/></label>
                        <input type="date" id="date" value=""/>
                    </div>
                    <div class="form-group">
                        <label for="restaurant" class="col-form-label" readonly="true"><spring:message
                                code="msg.menu.modal.form.restaurant"/></label>
                        <input type="text" id="restaurant" value=""/>
                    </div>
                </form>
                <div class="modalContainer">
                    <table class="table table-striped table-hover" id="modalDataTable" width="100%">
                        <thead>
                        <th><spring:message code="msg.menu.modal.table.name"/></th>
                        <th><spring:message code="msg.menu.modal.table.price"/></th>
                        <th/>
                        </thead>
                    </table>
                    <br>
                    <button type="button" class="btn btn-primary" id="addItemId">
                        <span class="fa fa-plus"></span>
                        <spring:message code="msg.btn.add"/>
                    </button>
                </div>
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
</body>
</html>
