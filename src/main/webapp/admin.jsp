<%-- Panel principal del administrador --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (!"admin".equals(session.getAttribute("rol"))) {
        response.sendRedirect("login.jsp?rol=admin");
        return;
    }
%>
<%@include file="lib/header.jsp"%>
<div class="container py-5">
    <h1 class="mb-4"><i class="bi bi-speedometer2 me-2"></i>Panel de administrador</h1>
    <p class="text-muted">Gestion de usuarios, productos y pedidos</p>
    <div class="row g-3">
        <div class="col-md-4">
            <div class="card h-100 shadow-sm text-center p-4">
                <i class="bi bi-people fs-1 text-primary mb-2"></i>
                <h5>Usuarios</h5>
                <a href="adminUsuarios.jsp" class="btn btn-primary btn-sm">Admin usuarios</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 shadow-sm text-center p-4">
                <i class="bi bi-box-seam fs-1 text-success mb-2"></i>
                <h5>Productos</h5>
                <a href="adminProductos.jsp" class="btn btn-success btn-sm">Admin productos</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 shadow-sm text-center p-4">
                <i class="bi bi-receipt fs-1 text-warning mb-2"></i>
                <h5>Pedidos</h5>
                <p class="small text-muted"></p>
                <a href="pedidos.jsp" class="btn btn-warning btn-sm">Ver pedidos</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 shadow-sm text-center p-4">
                <i class="bi bi-plus-circle fs-1 text-info mb-2"></i>
                <h5>Nuevo producto</h5>
                <a href="registroProductos.jsp" class="btn btn-outline-info btn-sm">Agregar</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 shadow-sm text-center p-4">
                <i class="bi bi-shop fs-1 text-secondary mb-2"></i>
                <h5>Catalogo</h5>
                <a href="catalogo.jsp" class="btn btn-outline-secondary btn-sm">Ver catalogo</a>
            </div>
        </div>
    </div>
</div>
<%@include file="lib/footer.jsp"%>
