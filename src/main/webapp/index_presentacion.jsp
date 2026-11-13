<%-- Pagina de inicio --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="lib/header.jsp"%>

<div class="container py-4">
    <section class="hero-store">
        <div class="row align-items-center">
            <div class="col-lg-7">
                <h1 class="display-5 fw-bold mb-3">MiTiendaVirtual</h1>
                <p class="lead mb-4">
                    Vende, gestiona y crece en linea. Publica productos y recibe pedidos
                </p>
                <div class="d-flex flex-wrap gap-2 justify-content-center justify-content-lg-start">
                    <a href="catalogo.jsp" class="btn btn-light btn-lg text-primary fw-semibold">
                        <i class="bi bi-shop me-1"></i>Ver catalogo
                    </a>
                    <a href="registroUsuarios.jsp" class="btn btn-outline-light btn-lg">
                        <i class="bi bi-person-plus me-1"></i>Crear cuenta
                    </a>
                </div>
            </div>
            <div class="col-lg-5 text-center">
                <div class="display-1"><i class="bi bi-bag-check-fill"></i></div>
            </div>
        </div>
    </section>

    <div class="row g-4 mb-5">
        <div class="col-md-4">
            <div class="card feature-card p-4 text-center">
                <div class="feature-icon mx-auto mb-3"><i class="bi bi-person-check"></i></div>
                <h5 class="fw-bold">Clientes</h5>
                <p class="text-muted mb-3">Registrate, inicia sesion y compra productos del catalogo</p>
                <a href="login.jsp?rol=cliente" class="btn btn-success btn-sm">Entrar como cliente</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card feature-card p-4 text-center">
                <div class="feature-icon mx-auto mb-3" style="background:linear-gradient(135deg,#ffc107,#fd7e14);">
                    <i class="bi bi-shield-lock"></i>
                </div>
                <h5 class="fw-bold">Administradores</h5>
                <p class="text-muted mb-3">Gestiona usuarios y productos</p>
                <a href="login.jsp?rol=admin" class="btn btn-warning btn-sm">Login admin</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card feature-card p-4 text-center">
                <div class="feature-icon mx-auto mb-3" style="background:linear-gradient(135deg,#20c997,#0dcaf0);">
                    <i class="bi bi-cart4"></i>
                </div>
                <h5 class="fw-bold">Catalogo</h5>
                <p class="text-muted mb-3">Los productos que registra el admin aparecen para que el cliente los compre</p>
                <a href="catalogo.jsp" class="btn btn-primary btn-sm">Ver productos</a>
            </div>
        </div>
    </div>
</div>

<%@include file="lib/footer.jsp"%>
