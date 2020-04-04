<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="msg.user.title"/></title>
    <jsp:include page="scripts.jsp"/>
</head>
<body>
<script type="text/javascript" src="resources/js/user.js" defer></script>
<script type="text/javascript" src="resources/js/common.js" defer></script>
<nav class="navbar navbar-expand-md navbar-dark bg-dark justify-content-end">
    <a class="btn btn-primary" href="logout">
        <spring:message code="msg.logout"/>
        <i class="fa fa-sign-out"></i>
    </a>
</nav>
<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="msg.user.head.title"/></h3>
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
                    </div>
                </form>
            </div>
            <div class="card-footer text-right">
                <button class="btn btn-danger" onclick="clearFilter()">
                    <span class="fa fa-remove"></span>
                    <spring:message code="msg.btn.cancel"/>
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
            <th><spring:message code="msg.user.head.table.restaurant"/></th>
            <th><spring:message code="msg.user.head.table.menu"/></th>
            <th><spring:message code="msg.user.head.table.date"/></th>
            <th><spring:message code="msg.user.head.table.vote"/></th>
            </thead>
        </table>
    </div>
</div>

<!-- The Modal -->
<div class="modal fade" id="mainForm">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modalContainer">
                <h3 class="text-center"><spring:message code="msg.user.view.form.title"/></h3>
                <table class="table table-striped table-hover" id="modalDataTable" width="100%">
                    <thead>
                    <th><spring:message code="msg.menu.modal.table.name"/></th>
                    <th><spring:message code="msg.menu.modal.table.price"/></th>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>
