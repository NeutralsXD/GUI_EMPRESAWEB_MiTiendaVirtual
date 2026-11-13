<%-- Alta de producto (solo admin) --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (!"admin".equals(session.getAttribute("rol"))) {
        response.sendRedirect("login.jsp?rol=admin");
        return;
    }
    String error = request.getParameter("error");
%>
<%@include file="lib/header.jsp"%>
<div class="container py-4" style="max-width:560px;">
    <div class="card shadow-sm">
        <div class="card-body p-4">
            <h2 class="text-center text-primary mb-3">Registrar producto</h2>
            <% if (error != null) { %>
            <div class="alert alert-danger"><%= error.replace("+", " ") %></div>
            <% } %>
            <form action="Servlet" method="POST">
                <input type="hidden" name="accion" value="agregarProducto">
                <div class="mb-3">
                    <label class="form-label">Nombre del producto *</label>
                    <input type="text" class="form-control" name="nombre" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Descripcion</label>
                    <textarea class="form-control" name="descripcion" rows="3"></textarea>
                </div>
                <div class="mb-3">
                    <label class="form-label">Categoria</label>
                    <input type="text" class="form-control" name="categoria" placeholder="Ej: Electronica, Ropa, Hogar">
                </div>
                <div class="mb-3">
                    <label class="form-label">Precio *</label>
                    <input type="number" step="0.01" min="0" class="form-control" name="precio" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Stock *</label>
                    <input type="number" min="0" class="form-control" name="stock" required>
                </div>
                <button type="submit" class="btn btn-success w-100">Registrar producto</button>
            </form>
            <p class="text-center mt-3 mb-0"><a href="adminProductos.jsp">Volver al listado</a></p>
        </div>
    </div>
</div>
<%@include file="lib/footer.jsp"%>
