<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="msg.restaurant.title"/></title>
    <jsp:include page="scripts.jsp"/>
</head>
<body>
<script type="text/javascript" src="resources/js/admin.js" defer></script>
<script type="text/javascript" src="resources/js/common.js" defer></script>
<nav class="navbar navbar-expand-md navbar-dark bg-dark justify-content-center">
    <ul class="navbar-nav mx-auto text-center">
        <li class="nav-itemModel">
            <a class="nav-link" href="menu"><spring:message code="msg.menu"/></a>
        </li>
        <li class="nav-itemModel">
            <a class="nav-link" href="users"><spring:message code="msg.user"/></a>
        </li>
    </ul>
    <a class="btn btn-primary" href="logout">
        <spring:message code="msg.logout"/>
        <i class="fa fa-sign-out"></i>
    </a>
</nav>
<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="msg.restaurant.title"/></h3>
        <table class="table table-striped table-hover" id="datatable">
            <thead>
            <th><spring:message code="msg.admin.head.table.name"/></th>
            <th><spring:message code="msg.admin.head.table.address"/></th>
            <th/>
            <th/>
            </thead>
        </table>
        <button type="button" class="btn btn-primary" id="createId"><spring:message code="msg.btn.add"/></button>
    </div>
</div>
<!-- The Modal -->
<div class="modal fade" id="mainForm">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title"><spring:message code="msg.admin.edit.form.title"/></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <!-- Modal body -->
            <div class="modal-body">
                <form action="" method="POST" id="modalForm">
                    <input type="hidden" id="restaurantId" value="">
                    <div class="form-group">
                        <label for="restaurant"><spring:message code="msg.admin.edit.form.restaurant"/></label>
                        <input type="text" id="restaurant" value=""/>
                    </div>
                    <div class="form-group">
                        <label for="address"><spring:message code="msg.admin.edit.form.address"/></label>
                        <input type="text" id="address" value=""/>
                    </div>
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
