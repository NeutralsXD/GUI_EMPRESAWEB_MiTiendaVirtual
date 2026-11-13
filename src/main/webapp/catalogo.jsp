<%-- Catalogo de productos --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, Modelo.*"%>
<%@include file="lib/header.jsp"%>
<%
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
            ? gp.filtrar("nombre", filtro)
            : gp.listarTodos();
    String rol = (String) session.getAttribute("rol");
    String msg = request.getParameter("msg");
    String error = request.getParameter("error");
%>

<div class="container py-4">
    <h2 class="text-primary mb-3">Catalogo de productos</h2>
    <% if (msg != null) { %><div class="alert alert-success"><%= msg.replace("+", " ") %></div><% } %>
    <% if (error != null) { %><div class="alert alert-danger"><%= error.replace("+", " ") %></div><% } %>

    <form class="row g-2 mb-4" method="get">
        <div class="col-auto flex-grow-1">
            <input type="text" name="filtro" class="form-control" placeholder="Buscar producto..."
                   value="<%= filtro != null ? filtro : "" %>">
        </div>
        <div class="col-auto">
            <button class="btn btn-primary" type="submit">Buscar</button>
            <a href="catalogo.jsp" class="btn btn-outline-secondary">Ver todos</a>
        </div>
    </form>

    <div class="row g-3">
    <% if (lista.isEmpty()) { %>
        <div class="col-12 text-center text-muted py-5">No hay productos en el catalogo</div>
    <% } else {
        for (Producto p : lista) { %>
        <div class="col-md-4 col-sm-6">
            <div class="card h-100 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title"><%= p.getNombre() %></h5>
                    <p class="card-text text-muted small"><%= p.getDescripcion() != null ? p.getDescripcion() : "" %></p>
                    <p class="mb-1"><span class="badge bg-secondary"><%= p.getCategoria() %></span></p>
                    <p class="fs-5 text-primary fw-bold mb-1">$<%= String.format(java.util.Locale.US, "%.2f", p.getPrecio()) %></p>
                    <p class="small">Stock: <%= p.getStock() %></p>
                </div>
                <div class="card-footer bg-white border-0">
                    <% if ("cliente".equals(rol) && p.getStock() > 0) { %>
                    <form action="Servlet" method="POST">
                        <input type="hidden" name="accion" value="agregarCarrito">
                        <input type="hidden" name="idProducto" value="<%= p.getId() %>">
                        <button type="submit" class="btn btn-success w-100">Agregar al carrito</button>
                    </form>
                    <% } else if (!"cliente".equals(rol)) { %>
                    <a href="login.jsp?rol=cliente" class="btn btn-outline-primary w-100">Inicia sesion para comprar</a>
                    <% } else { %>
                    <button class="btn btn-secondary w-100" disabled>Sin stock</button>
                    <% } %>
                </div>
            </div>
        </div>
    <% } } %>
    </div>
</div>
<%@include file="lib/footer.jsp"%>
