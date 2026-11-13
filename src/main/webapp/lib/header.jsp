
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%

    String rolSesion = (String) session.getAttribute("rol");
    String nombreSesion = (String) session.getAttribute("nombreUsuario");
    if (nombreSesion == null) {
        nombreSesion = (String) session.getAttribute("usuario");
    }

    java.util.List carritoNav = (java.util.List) session.getAttribute("carrito");
    int itemsCarrito = carritoNav != null ? carritoNav.size() : 0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MiTiendaVirtual</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="./styles/styles.css" rel="stylesheet" type="text/css">
</head>
<body class="d-flex flex-column min-vh-100">
<nav class="navbar navbar-expand-lg navbar-dark navbar-store sticky-top">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center gap-2 fw-bold" href="index_presentacion.jsp">
            <i class="bi bi-bag-heart-fill fs-4"></i>
            <span>MiTiendaVirtual</span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="index_presentacion.jsp"><i class="bi bi-house-door me-1"></i>Inicio</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="catalogo.jsp"><i class="bi bi-shop me-1"></i>Catalogo</a>
                </li>
                <% if ("admin".equals(rolSesion)) { %>
                <li class="nav-item">
                    <a class="nav-link" href="admin.jsp"><i class="bi bi-speedometer2 me-1"></i>Panel</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="adminUsuarios.jsp"><i class="bi bi-people me-1"></i>Usuarios</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="adminProductos.jsp"><i class="bi bi-box-seam me-1"></i>Productos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="pedidos.jsp"><i class="bi bi-receipt me-1"></i>Pedidos</a>
                </li>
                <% } %>
                <% if ("cliente".equals(rolSesion)) { %>
                <li class="nav-item">
                    <a class="nav-link" href="carrito.jsp">
                        <i class="bi bi-cart me-1"></i>Carrito
                        <% if (itemsCarrito > 0) { %>
                        <span class="badge text-bg-warning"><%= itemsCarrito %></span>
                        <% } %>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="pedidos.jsp"><i class="bi bi-bag-check me-1"></i>Mis pedidos</a>
                </li>
                <% } %>
            </ul>

            <div class="d-flex flex-wrap align-items-center gap-2 ms-lg-auto">
                <% if (rolSesion != null) { %>
                    <span class="text-white-50 small">
                        <i class="bi bi-person-circle me-1"></i><%= nombreSesion %> (<%= rolSesion %>)
                    </span>
                    <form action="Servlet" method="POST" class="d-inline">
                        <input type="hidden" name="accion" value="logout">
                        <button type="submit" class="btn btn-outline-light btn-sm">Salir</button>
                    </form>
                <% } else { %>
                    <a href="login.jsp?rol=cliente" class="btn btn-outline-light btn-sm">Login cliente</a>
                    <a href="login.jsp?rol=admin" class="btn btn-warning btn-sm">Login admin</a>
                    <a href="registroUsuarios.jsp" class="btn btn-light btn-sm text-primary">Registrarse</a>
                <% } %>
            </div>
        </div>
    </div>
</nav>
