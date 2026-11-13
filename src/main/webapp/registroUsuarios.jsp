<%-- Registro de un nuevo cliente --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="lib/header.jsp"%>
<%
    String error = request.getParameter("error");
%>
<div class="container py-4" style="max-width:560px;">
    <div class="card shadow-sm">
        <div class="card-body p-4">
            <h2 class="text-center text-primary mb-3">Registro de Cliente</h2>
            <% if (error != null) { %>
            <div class="alert alert-danger"><%= error.replace("+", " ") %></div>
            <% } %>
            <form action="Servlet" method="POST">
                <input type="hidden" name="accion" value="registroUsuario">
                <div class="mb-3">
                    <label class="form-label">Nombre completo *</label>
                    <input type="text" class="form-control" name="nombre" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Correo electronico *</label>
                    <input type="email" class="form-control" name="email" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Usuario *</label>
                    <input type="text" class="form-control" name="usuario" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Contraseña *</label>
                    <input type="password" class="form-control" name="contrasena" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Telefono</label>
                    <input type="text" class="form-control" name="telefono">
                </div>
                <div class="mb-3">
                    <label class="form-label">Direccion</label>
                    <input type="text" class="form-control" name="direccion">
                </div>
                <button type="submit" class="btn btn-primary w-100">Registrar</button>
            </form>
            <p class="text-center mt-3 mb-0"><a href="login.jsp?rol=cliente">Ya tienes cuenta? Inicia sesion</a></p>
        </div>
    </div>
</div>
<%@include file="lib/footer.jsp"%>
