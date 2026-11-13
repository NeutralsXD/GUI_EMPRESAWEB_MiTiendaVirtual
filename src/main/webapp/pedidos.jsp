<%-- listado de pedidos --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, Modelo.*"%>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String dataPath = application.getRealPath("/WEB-INF/data");
    if (dataPath == null) {
        dataPath = System.getProperty("java.io.tmpdir") + "/mitienda_data";
    }
    GestionarPedidos gped = (GestionarPedidos) application.getAttribute("gestPedidos");
    if (gped == null) {
        gped = new GestionarPedidos(dataPath);
        application.setAttribute("gestPedidos", gped);
    }
    List<Pedido> lista = gped.listarTodos();
    String idUsuario = (String) session.getAttribute("idUsuario");
    if (!"admin".equals(rol) && idUsuario != null) {
        List<Pedido> propios = new ArrayList<>();
        for (Pedido p : lista) {
            if (idUsuario.equals(p.getIdCliente())) {
                propios.add(p);
            }
        }
        lista = propios;
    }
    String reporte = "";
    try {
        reporte = gped.leerUltimoReporte();
    } catch (Exception e) {
        reporte = "Sin reportes.";
    }
%>
<%@include file="lib/header.jsp"%>

<div class="container py-4">
    <h2 class="text-primary mb-3">Pedidos y facturas</h2>
    <p class="text-muted">Asociacion entre cliente (Usuario) y productos comprados.</p>

    <div class="table-responsive mb-4">
        <table class="table table-striped table-bordered align-middle">
            <thead class="table-dark">
                <tr>
                    <th>ID</th><th>Cliente</th><th>Metodo pago</th><th>Detalle</th>
                    <th>Total</th><th>Estado</th>
                </tr>
            </thead>
            <tbody>
            <% if (lista.isEmpty()) { %>
                <tr><td colspan="6" class="text-center text-muted">No hay pedidos.</td></tr>
            <% } else {
                for (Pedido p : lista) { %>
                <tr>
                    <td><%= p.getId() %></td>
                    <td><%= p.getNombreCliente() %></td>
                    <td><%= p.getMetodoPago() %></td>
                    <td><%= p.getDetallePago() != null ? p.getDetallePago() : "-" %></td>
                    <td>$<%= String.format(java.util.Locale.US, "%.2f", p.getTotal()) %></td>
                    <td><%= p.getEstado() %></td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>

    <% if ("admin".equals(rol)) { %>
    <div class="card">
        <div class="card-header">Contenido de reporte</div>
        <div class="card-body">
            <pre class="mb-0 small" style="white-space:pre-wrap;max-height:400px;overflow:auto;"><%= reporte %></pre>
        </div>
    </div>
    <% } %>
</div>
<%@include file="lib/footer.jsp"%>
