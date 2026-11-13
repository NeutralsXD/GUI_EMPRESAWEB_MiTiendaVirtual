<%-- Carrito y confirmacion de compra con metodo de pago --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@include file="lib/header.jsp"%>
<%
    String rol = (String) session.getAttribute("rol");
    @SuppressWarnings("unchecked")
    List<Map<String, String>> carrito = (List<Map<String, String>>) session.getAttribute("carrito");
    if (carrito == null) {
        carrito = new ArrayList<>();
    }
    double total = 0;
    for (Map<String, String> item : carrito) {
        try {
            total += Double.parseDouble(item.get("precio").replace(",", "."));
        } catch (Exception e) {}
    }
    String msg = request.getParameter("msg");
    String error = request.getParameter("error");
%>

<div class="container py-5">
    <h1 class="section-title mb-4"><i class="bi bi-bag-check me-2"></i>Mi carrito </h1>

    <% if (msg != null) { %><div class="alert alert-success"><%= msg.replace("+", " ") %></div><% } %>
    <% if (error != null) { %><div class="alert alert-danger"><%= error.replace("+", " ") %></div><% } %>

    <% if (!"cliente".equals(rol)) { %>
    <div class="alert alert-warning">
        Debes <a href="login.jsp?rol=cliente" class="alert-link">iniciar sesion como cliente</a> para usar el carrito
    </div>
    <% } %>

    <div class="card border-0 shadow-sm rounded-4 mb-4">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-light">
                    <tr>
                        <th>Producto</th>
                        <th>Precio</th>
                        <th class="text-end">Accion</th>
                    </tr>
                </thead>
                <tbody>
                <% if (carrito.isEmpty()) { %>
                    <tr>
                        <td colspan="3" class="text-center text-muted py-4">
                            Tu carrito esta vacio <a href="catalogo.jsp">Ir al catalogo</a>
                        </td>
                    </tr>
                <% } else {
                    int idx = 0;
                    for (Map<String, String> item : carrito) {
                %>
                    <tr>
                        <td>
                            <strong><%= item.get("nombre") %></strong>
                            <div class="small text-muted"><%= item.get("descripcion") != null ? item.get("descripcion") : "" %></div>
                        </td>
                        <td class="text-primary fw-bold">$<%= item.get("precio") %></td>
                        <td class="text-end">
                            <% if ("cliente".equals(rol)) { %>
                            <form action="Servlet" method="POST" class="d-inline">
                                <input type="hidden" name="accion" value="quitarCarrito">
                                <input type="hidden" name="index" value="<%= idx %>">
                                <button type="submit" class="btn btn-sm btn-outline-danger">Quitar</button>
                            </form>
                            <% } %>
                        </td>
                    </tr>
                <%
                        idx++;
                    }
                } %>
                </tbody>
                <% if (!carrito.isEmpty()) { %>
                <tfoot>
                    <tr>
                        <th>Total</th>
                        <th colspan="2" class="text-primary fs-5">
                            $<%= String.format(java.util.Locale.US, "%.2f", total) %>
                        </th>
                    </tr>
                </tfoot>
                <% } %>
            </table>
        </div>
    </div>

    <% if ("cliente".equals(rol) && !carrito.isEmpty()) { %>
    <div class="card border-0 shadow-sm rounded-4">
        <div class="card-body p-4">
            <h5 class="mb-3"><i class="bi bi-credit-card me-2"></i>Metodo de pago</h5>
            <form action="Servlet" method="POST" id="formCompra">
                <input type="hidden" name="accion" value="comprar">

                <div class="mb-3">
                    <label class="form-label fw-semibold">Con que vas a pagar?</label>
                    <select name="metodoPago" id="metodoPago" class="form-select form-select-lg" required
                            onchange="mostrarDetallePago()">
                        <option value="">Selecciona metodo de pago</option>
                        <option value="Efectivo">Efectivo</option>
                        <option value="Tarjeta">Tarjeta debito/credito</option>
                        <option value="PSE">PSE</option>
                        <option value="Nequi">Nequi</option>
                        <option value="Transferencia">Transferencia bancaria</option>
                    </select>
                </div>
                <div class="mb-3" id="detallePagoBox" style="display:none;">
                    <label class="form-label fw-semibold" id="labelDetalle">Detalle del pago</label>
                    <input type="text" class="form-control" name="detallePago" id="detallePago"
                           placeholder="Numero de tarjeta, referencia PSE, celular Nequi, etc.">
                    <div class="form-text" id="ayudaDetalle"></div>
                </div>

                <div class="d-flex gap-2 flex-wrap">
                    <button type="submit" class="btn btn-success btn-lg">
                        Confirmar pedido ($<%= String.format(java.util.Locale.US, "%.2f", total) %>)
                    </button>
                    <a href="catalogo.jsp" class="btn btn-outline-secondary btn-lg">Seguir comprando</a>
                </div>
            </form>
        </div>
    </div>
    <script>
    function mostrarDetallePago() {
        var m = document.getElementById('metodoPago').value;
        var box = document.getElementById('detallePagoBox');
        var label = document.getElementById('labelDetalle');
        var input = document.getElementById('detallePago');
        if (m === 'Efectivo' || m === '') {
            box.style.display = 'none';
            input.required = false;
            input.value = '';
        } else {
            box.style.display = 'block';
            input.required = true;
            if (m === 'Tarjeta') {
                label.textContent = 'Numero de tarjeta (ultimos 4 digitos o referencia)';
                input.placeholder = 'Ultimos 4 digitos o autorizacion';
            } else if (m === 'PSE') {
                label.textContent = 'Banco / referencia PSE';
                input.placeholder = 'Banco o referencia PSE';
            } else if (m === 'Nequi') {
                label.textContent = 'Celular Nequi';
                input.placeholder = '3001234567';
            } else if (m === 'Transferencia') {
                label.textContent = 'Cuenta o referencia de transferencia';
                input.placeholder = 'Cuenta / referencia';
            }
        }
    }
    </script>
    <% } %>
</div>

<%@include file="lib/footer.jsp"%>
