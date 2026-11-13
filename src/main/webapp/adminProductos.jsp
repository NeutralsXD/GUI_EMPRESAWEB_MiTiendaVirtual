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
    GestionarProductos gp = (GestionarProductos) application.getAttribute("gestProductos");
    if (gp == null) {
        gp = new GestionarProductos(dataPath);
        application.setAttribute("gestProductos", gp);
    }
    String filtro = request.getParameter("filtro");
    List<Producto> lista = (filtro != null && !filtro.trim().isEmpty())
            ? gp.filtrar(request.getParameter("campo"), filtro)
            : gp.listarTodos();
    String ok = request.getParameter("ok");
    String error = request.getParameter("error");
    String editId = request.getParameter("edit");
    Producto editProd = (editId != null) ? gp.buscarPorId(editId) : null;
%>
<%@include file="lib/header.jsp"%>

<div class="container py-4">
    <h2 class="text-primary mb-3">Administración de Productos</h2>
    <% if (ok != null) { %><div class="alert alert-success"><%= ok.replace("+", " ") %></div><% } %>
    <% if (error != null) { %><div class="alert alert-danger"><%= error.replace("+", " ") %></div><% } %>

    <form class="row g-2 mb-3" method="get">
               
        <div class="col-auto">

            <a href="registroProductos.jsp" class="btn btn-success">Nuevo producto</a>
        </div>
    </form>

    <div class="table-responsive mb-4">
        <table class="table table-striped table-hover table-bordered align-middle">
            <thead class="table-dark">
                <tr>
                    <th>ID</th><th>Nombre</th><th>Descripcion</th>
                    <th>Precio</th><th>Categoria</th><th>Stock</th><th>Acciones</th>
                </tr>
            </thead>
            <tbody>
            <% if (lista.isEmpty()) { %>
                <tr><td colspan="7" class="text-center text-muted">No hay productos registrados</td></tr>
            <% } else {
                for (Producto p : lista) { %>
                <tr>
                    <td><%= p.getId() %></td>
                    <td><%= p.getNombre() %></td>
                    <td><%= p.getDescripcion() != null ? p.getDescripcion() : "" %></td>
                    <td>$<%= String.format(java.util.Locale.US, "%.2f", p.getPrecio()) %></td>
                    <td><%= p.getCategoria() %></td>
                    <td><%= p.getStock() %></td>
                    <td class="text-nowrap">
                        <a href="adminProductos.jsp?edit=<%= p.getId() %>" class="btn btn-sm btn-warning">Editar</a>
                        <form action="Servlet" method="POST" class="d-inline"
                              onsubmit="return confirm('Eliminar producto?');">
                            <input type="hidden" name="accion" value="eliminarProducto">
                            <input type="hidden" name="id" value="<%= p.getId() %>">
                            <button type="submit" class="btn btn-sm btn-danger">Eliminar</button>
                        </form>
                    </td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>

    <% if (editProd != null) { %>
    <div class="card shadow-sm">
        <div class="card-body">
            <h5>Editar producto #<%= editProd.getId() %></h5>
            <form action="Servlet" method="POST" class="row g-3">
                <input type="hidden" name="accion" value="editarProducto">
                <input type="hidden" name="id" value="<%= editProd.getId() %>">
                <div class="col-md-6">
                    <label class="form-label">Nombre *</label>
                    <input type="text" name="nombre" class="form-control" required value="<%= editProd.getNombre() %>">
                </div>
                <div class="col-md-6">
                    <label class="form-label">Categoria</label>
                    <input type="text" name="categoria" class="form-control" value="<%= editProd.getCategoria() %>">
                </div>
                <div class="col-12">
                    <label class="form-label">Descripcion</label>
                    <textarea name="descripcion" class="form-control" rows="2"><%= editProd.getDescripcion() != null ? editProd.getDescripcion() : "" %></textarea>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Precio *</label>
                    <input type="number" step="0.01" name="precio" class="form-control" required value="<%= editProd.getPrecio() %>">
                </div>
                <div class="col-md-6">
                    <label class="form-label">Stock *</label>
                    <input type="number" name="stock" class="form-control" required value="<%= editProd.getStock() %>">
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-primary">Actualizar</button>
                    <a href="adminProductos.jsp" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
    <% } %>
</div>
<%@include file="lib/footer.jsp"%>
