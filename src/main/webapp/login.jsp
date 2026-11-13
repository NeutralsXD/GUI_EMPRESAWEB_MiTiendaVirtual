<%-- Login de cliente o administrador --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="lib/header.jsp"%>
<%
    String rolPref = request.getParameter("rol");
    if (rolPref == null) {
        rolPref = "cliente";
    }
    boolean esAdmin = "admin".equals(rolPref);
    String error = request.getParameter("error");
    String ok = request.getParameter("ok");
%>

<div class="form-card">
    <div class="text-center mb-3">
        <% if (esAdmin) { %>
        <span class="badge text-bg-warning mb-2"><i class="bi bi-shield-lock"></i> Administrador</span>
        <h2>Login administrador</h2>
        <% } else { %>
        <span class="badge text-bg-success mb-2"><i class="bi bi-person"></i> Cliente</span>
        <h2>Login cliente</h2>
        <% } %>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-danger py-2"><%= error.replace("+", " ") %></div>
    <% } %>
    <% if (ok != null) { %>
    <div class="alert alert-success py-2"><%= ok.replace("+", " ") %></div>
    <% } %>

    <form action="Servlet" method="POST">
        <input type="hidden" name="accion" value="login">
        <input type="hidden" name="rolPreferido" value="<%= esAdmin ? "admin" : "cliente" %>">
        <div class="mb-3">
            <label for="usuario" class="form-label">Usuario</label>
            <input type="text" class="form-control" id="usuario" name="usuario" required>
        </div>
        <div class="mb-3">
            <label for="contrasena" class="form-label">Contraseña</label>
            <input type="password" class="form-control" id="contrasena" name="contrasena" required>
        </div>
        <button type="submit" class="btn <%= esAdmin ? "btn-warning" : "btn-success" %> w-100">
            <i class="bi bi-box-arrow-in-right me-1"></i>Entrar
        </button>
    </form>
    <% if (!esAdmin) { %>
    <p class="mt-3 text-center mb-0 small text-muted">
        No tienes cuenta? <a href="registroUsuarios.jsp">Registrate aqui</a>
    </p>
    <% } %>
    <p class="mt-2 text-center mb-0">
        <a href="index_presentacion.jsp" class="small">Volver al inicio</a>
    </p>
</div>

<%@include file="lib/footer.jsp"%>
