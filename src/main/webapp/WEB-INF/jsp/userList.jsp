<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="msg.user.list.title"/></title>
    <jsp:include page="scripts.jsp"/>
</head>
<body>
<script type="text/javascript" src="resources/js/userList.js" defer></script>
<script type="text/javascript" src="resources/js/common.js" defer></script>
<nav class="navbar navbar-expand-md navbar-dark bg-dark justify-content-center">
    <ul class="navbar-nav mx-auto text-center">
        <li class="nav-itemModel">
            <a class="nav-link active" href="admin"><spring:message code="msg.restaurant"/></a>
        </li>
        <li class="nav-itemModel">
            <a class="nav-link" href="menu"><spring:message code="msg.menu"/></a>
        </li>
        <li class="nav-itemModel">
            <a class="nav-link" href="javascript:history.back()"><spring:message code="msg.btn.back"/></a>
        </li>
    </ul>
    <a class="btn btn-primary" href="logout">
        <spring:message code="msg.logout"/>
        <i class="fa fa-sign-out"></i>
    </a>
</nav>
<div class="jumbotron pt-4">
    <h3 class="text-center"><spring:message code="msg.user.list.header"/></h3>
    <br>
    <table class="table table-striped table-hover" id="datatable">
        <thead>
        <th><spring:message code="msg.user.list.head.table.name"/></th>
        <th><spring:message code="msg.user.list.head.table.email"/></th>
        <th><spring:message code="msg.user.list.head.table.enabled"/></th>
        <th/>
        <th/>
        </thead>
    </table>
    <br>
    <button type="button" class="btn btn-primary" id="createId">
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
                <h4 class="modal-title"><spring:message
                        code="msg.user.list.edit.form.title"/></h4>
            </div>
            <!-- Modal body -->
            <div class="modal-body">
                <form action="" id="modalForm">
                    <input type="hidden" id="userId" value="">
                    <div class="form-group">
                        <label for="user" class="col-form-label"><spring:message
                                code="msg.user.list.form.user"/></label>
                        <input type="text" id="user" value=""/>
                    </div>
                    <div class="form-group">
                        <label for="email" class="col-form-label"><spring:message
                                code="msg.user.list.form.email"/></label>
                        <input type="email" id="email" value=""/>
                    </div>
                    <div class="form-group">
                        <label for="enabled" class="col-form-label"><spring:message
                                code="msg.user.list.form.enabled"/></label>
                        <input type="checkbox" id="enabled" value=""/>
                    </div>
                    <div class="form-group">
                        <label for="enabled" class="col-form-label"><spring:message
                                code="msg.user.list.form.role"/></label>
                        <select class="custom-select" name="roles" id="rolesSelectId" onchange="changeRoles()">
                            <option datatype="text" name="role"></option>
                            <option datatype="text" name="role" value="ROLE_ADMIN">ADMIN</option>
                            <option datatype="text" name="role" value="ROLE_USER">USER</option>
                        </select>
                    </div>
                    <label for="enabled" class="col-form-label"><spring:message
                            code="msg.user.list.form.current.role"/>:</label>
                    <div class="form-group">
                        <input type="text" id="roleListId" value="" readonly="readonly"/>
                    </div>
                </form>
                <div class="modalContainer">
                    <h2 class="text-center"><spring:message code="msg.voting.history"/></h2>
                    <table class="table table-striped table-hover" id="modalDataTable" width="100%">
                        <thead>
                        <th><spring:message code="msg.user.list.head.form.table.menu"/></th>
                        <th><spring:message code="msg.user.list.head.form.table.datetime"/></th>
                        <th><spring:message code="msg.user.list.head.form.table.state"/></th>
                        </thead>
                    </table>
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
