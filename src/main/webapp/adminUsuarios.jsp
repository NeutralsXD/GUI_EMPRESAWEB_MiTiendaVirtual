<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, Modelo.*"%>
<%
    String rol = (String) session.getAttribute("rol");
    if (!"admin".equals(rol)) {
        response.sendRedirect("login.jsp?rol=admin");
        return;
    }
    String dataPath = application.getRealPath("/WEB-INF/data");
    if (dataPath == null) {
        dataPath = System.getProperty("java.io.tmpdir") + "/mitienda_data";
    }
    GestionarUsuarios gu = (GestionarUsuarios) application.getAttribute("gestUsuarios");
    if (gu == null) {
        gu = new GestionarUsuarios(dataPath);
        application.setAttribute("gestUsuarios", gu);
    }
    String filtro = request.getParameter("filtro");
    List<Usuario> lista = (filtro != null && !filtro.trim().isEmpty())
            ? gu.filtrar(request.getParameter("campo"), filtro)
            : gu.listarTodos();
    String ok = request.getParameter("ok");
    String error = request.getParameter("error");
    String editId = request.getParameter("edit");
    Usuario editUser = (editId != null) ? gu.buscarPorId(editId) : null;
%>
<%@include file="lib/header.jsp"%>

<div class="container py-4">
    <h2 class="text-primary mb-3">Administración de Usuarios</h2>
    <% if (ok != null) { %><div class="alert alert-success"><%= ok.replace("+", " ") %></div><% } %>
    <% if (error != null) { %><div class="alert alert-danger"><%= error.replace("+", " ") %></div><% } %>

    <form class="row g-2 mb-3" method="get">

    </form>

    <div class="table-responsive mb-4">
        <table class="table table-striped table-hover table-bordered align-middle">
            <thead class="table-dark">
                <tr>
                    <th>ID</th><th>Nombre</th><th>Usuario</th><th>Email</th>
                    <th>Telefono</th><th>Rol</th><th>Acciones</th>
                </tr>
            </thead>
            <tbody>
            <% if (lista.isEmpty()) { %>
                <tr><td colspan="7" class="text-center text-muted">No hay usuarios registrados.</td></tr>
            <% } else {
                for (Usuario u : lista) { %>
                <tr>
                    <td><%= u.getId() %></td>
                    <td><%= u.getNombre() %></td>
                    <td><%= u.getUsuario() %></td>
                    <td><%= u.getEmail() %></td>
                    <td><%= u.getTelefono() != null ? u.getTelefono() : "" %></td>
                    <td>
                        <span class="badge bg-<%= "admin".equals(u.getRol()) ? "danger" : "primary" %>">
                            <%= u.getRol() %>
                        </span>
                    </td>
                    <td class="text-nowrap">
                        <a href="adminUsuarios.jsp?edit=<%= u.getId() %>" class="btn btn-sm btn-warning">Editar</a>
                        <form action="Servlet" method="POST" class="d-inline"
                              onsubmit="return confirm('Eliminar usuario?');">
                            <input type="hidden" name="accion" value="eliminarUsuario">
                            <input type="hidden" name="id" value="<%= u.getId() %>">
                            <button type="submit" class="btn btn-sm btn-danger">Eliminar</button>
                        </form>
                    </td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">
            <h5><%= editUser != null ? "Editar usuario" : "Agregar usuario" %></h5>
            <form action="Servlet" method="POST" class="row g-3">
                <input type="hidden" name="accion"
                       value="<%= editUser != null ? "editarUsuario" : "agregarUsuario" %>">
                <% if (editUser != null) { %>
                <input type="hidden" name="id" value="<%= editUser.getId() %>">
                <% } %>
                <div class="col-md-6">
                    <label class="form-label">Nombre completo *</label>
                    <input type="text" name="nombre" class="form-control" required
                           value="<%= editUser != null ? editUser.getNombre() : "" %>">
                </div>
                <div class="col-md-6">
                    <label class="form-label">Email *</label>
                    <input type="email" name="email" class="form-control" required
                           value="<%= editUser != null ? editUser.getEmail() : "" %>">
                </div>
                <div class="col-md-4">
                    <label class="form-label">Usuario *</label>
                    <input type="text" name="usuario" class="form-control" required
                           value="<%= editUser != null ? editUser.getUsuario() : "" %>">
                </div>
                <div class="col-md-4">
                    <label class="form-label">
                        Contraseña <%= editUser != null ? "(dejar vacio para no cambiar)" : "*" %>
                    </label>
                    <input type="password" name="contrasena" class="form-control"
                           <%= editUser == null ? "required" : "" %>>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Rol</label>
                    <select name="rol" class="form-select">
                        <option value="cliente" <%= editUser != null && "cliente".equals(editUser.getRol()) ? "selected" : "" %>>cliente</option>
                        <option value="admin" <%= editUser != null && "admin".equals(editUser.getRol()) ? "selected" : "" %>>admin</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Telefono</label>
                    <input type="text" name="telefono" class="form-control"
                           value="<%= editUser != null && editUser.getTelefono() != null ? editUser.getTelefono() : "" %>">
                </div>
                <div class="col-md-6">
                    <label class="form-label">Direccion</label>
                    <input type="text" name="direccion" class="form-control"
                           value="<%= editUser != null && editUser.getDireccion() != null ? editUser.getDireccion() : "" %>">
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-primary">
                        <%= editUser != null ? "Actualizar" : "Registrar" %>
                    </button>
                    <% if (editUser != null) { %>
                    <a href="adminUsuarios.jsp" class="btn btn-secondary">Cancelar</a>
                    <% } %>
                </div>
            </form>
        </div>
    </div>
</div>
<%@include file="lib/footer.jsp"%>
