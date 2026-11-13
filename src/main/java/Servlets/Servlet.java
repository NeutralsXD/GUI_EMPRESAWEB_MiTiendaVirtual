package Servlets;

import Modelo.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})
public class Servlet extends HttpServlet {

    
    private String dataDir(ServletContext ctx) {
        String path = ctx.getRealPath("/WEB-INF/data");
        if (path == null) {
            path = System.getProperty("java.io.tmpdir") + "/mitienda_data";
        }
        return path;
    }

    
    private GestionarUsuarios gestUsuarios(ServletContext ctx) throws Exception {
        GestionarUsuarios g = (GestionarUsuarios) ctx.getAttribute("gestUsuarios");
        if (g == null) {
            g = new GestionarUsuarios(dataDir(ctx));
            ctx.setAttribute("gestUsuarios", g);
        }
        return g;
    }

    
    private GestionarProductos gestProductos(ServletContext ctx) throws Exception {
        GestionarProductos g = (GestionarProductos) ctx.getAttribute("gestProductos");
        if (g == null) {
            g = new GestionarProductos(dataDir(ctx));
            ctx.setAttribute("gestProductos", g);
        }
        return g;
    }

    
    private GestionarPedidos gestPedidos(ServletContext ctx) throws Exception {
        GestionarPedidos g = (GestionarPedidos) ctx.getAttribute("gestPedidos");
        if (g == null) {
            g = new GestionarPedidos(dataDir(ctx));
            ctx.setAttribute("gestPedidos", g);
        }
        return g;
    }

    
    private String texto(String s) {
        return s == null ? "" : s.trim();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String accion = texto(request.getParameter("accion"));
        HttpSession session = request.getSession();
        ServletContext ctx = getServletContext();

        try {
            switch (accion) {

                //  LOGIN                 
                case "login": {
                    String user = texto(request.getParameter("usuario"));
                    String pass = texto(request.getParameter("contrasena"));
                    String rolPref = texto(request.getParameter("rolPreferido"));
                    if (rolPref.isEmpty()) {
                        rolPref = "cliente";
                    }

                    GestionarUsuarios gu = gestUsuarios(ctx);
                    Usuario u = gu.buscarPorUsuario(user);

                    // Credenciales invalidas
                    if (u == null || !pass.equals(u.getContrasena())) {
                        response.sendRedirect("login.jsp?rol=" + rolPref + "&error=Usuario+o+contrasena+incorrectos");
                        return;
                    }

                    String rolReal = u.getRol();
                    if ("admin".equals(rolPref) && !"admin".equals(rolReal)) {
                        response.sendRedirect("login.jsp?rol=admin&error=No+tienes+permiso+de+administrador");
                        return;
                    }
                    if ("cliente".equals(rolPref) && "admin".equals(rolReal)) {
                        response.sendRedirect("login.jsp?rol=cliente&error=Usa+el+login+de+administrador");
                        return;
                    }

                    session.setAttribute("usuario", u.getUsuario());
                    session.setAttribute("nombreUsuario", u.getNombre());
                    session.setAttribute("idUsuario", u.getId());
                    session.setAttribute("rol", rolReal);

                    if ("admin".equals(rolReal)) {
                        response.sendRedirect("admin.jsp");
                    } else {
                        response.sendRedirect("catalogo.jsp");
                    }
                    return;
                }

                //  LOGOUT 
                case "logout": {
                    session.invalidate(); // Borra toda la sesion
                    response.sendRedirect("index_presentacion.jsp");
                    return;
                }

                // USUARIO
                case "registroUsuario": {
                    GestionarUsuarios gu = gestUsuarios(ctx);
                    Usuario u = new Usuario();
                    u.setNombre(texto(request.getParameter("nombre")));
                    u.setEmail(texto(request.getParameter("email")));
                    u.setUsuario(texto(request.getParameter("usuario")));
                    u.setContrasena(texto(request.getParameter("contrasena")));
                    u.setTelefono(texto(request.getParameter("telefono")));
                    u.setDireccion(texto(request.getParameter("direccion")));
                    u.setRol("cliente");
                    try {
                        gu.agregar(u);
                        response.sendRedirect("login.jsp?rol=cliente&ok=Registro+exitoso,+ya+puedes+iniciar+sesion");
                    } catch (Exception ex) {
                        response.sendRedirect("registroUsuarios.jsp?error=" + encode(ex.getMessage()));
                    }
                    return;
                }

                case "agregarUsuario": {
                    // Alta desde el panel admin
                    if (!esAdmin(session)) {
                        response.sendRedirect("login.jsp?rol=admin");
                        return;
                    }
                    GestionarUsuarios gu = gestUsuarios(ctx);
                    Usuario u = new Usuario();
                    u.setNombre(texto(request.getParameter("nombre")));
                    u.setEmail(texto(request.getParameter("email")));
                    u.setUsuario(texto(request.getParameter("usuario")));
                    u.setContrasena(texto(request.getParameter("contrasena")));
                    u.setTelefono(texto(request.getParameter("telefono")));
                    u.setDireccion(texto(request.getParameter("direccion")));
                    String rol = texto(request.getParameter("rol"));
                    u.setRol(rol.isEmpty() ? "cliente" : rol);
                    try {
                        gu.agregar(u);
                        response.sendRedirect("adminUsuarios.jsp?ok=Usuario+registrado");
                    } catch (Exception ex) {
                        response.sendRedirect("adminUsuarios.jsp?error=" + encode(ex.getMessage()));
                    }
                    return;
                }

                case "editarUsuario": {
                    if (!esAdmin(session)) {
                        response.sendRedirect("login.jsp?rol=admin");
                        return;
                    }
                    GestionarUsuarios gu = gestUsuarios(ctx);
                    Usuario u = new Usuario();
                    u.setId(texto(request.getParameter("id")));
                    u.setNombre(texto(request.getParameter("nombre")));
                    u.setEmail(texto(request.getParameter("email")));
                    u.setUsuario(texto(request.getParameter("usuario")));
                    u.setContrasena(texto(request.getParameter("contrasena")));
                    u.setTelefono(texto(request.getParameter("telefono")));
                    u.setDireccion(texto(request.getParameter("direccion")));
                    u.setRol(texto(request.getParameter("rol")));
                    try {
                        gu.actualizar(u);
                        response.sendRedirect("adminUsuarios.jsp?ok=Perfil+actualizado+correctamente");
                    } catch (Exception ex) {
                        response.sendRedirect("adminUsuarios.jsp?error=" + encode(ex.getMessage()));
                    }
                    return;
                }

                case "eliminarUsuario": {
                    if (!esAdmin(session)) {
                        response.sendRedirect("login.jsp?rol=admin");
                        return;
                    }
                    try {
                        gestUsuarios(ctx).eliminar(texto(request.getParameter("id")));
                        response.sendRedirect("adminUsuarios.jsp?ok=Usuario+eliminado");
                    } catch (Exception ex) {
                        response.sendRedirect("adminUsuarios.jsp?error=" + encode(ex.getMessage()));
                    }
                    return;
                }

                // PRODUCTOS
                case "registroProducto":
                case "agregarProducto": {
                    if (!esAdmin(session)) {
                        response.sendRedirect("login.jsp?rol=admin");
                        return;
                    }
                    GestionarProductos gp = gestProductos(ctx);
                    Producto p = new Producto();
                    p.setNombre(texto(request.getParameter("nombre")));
                    p.setDescripcion(texto(request.getParameter("descripcion")));
                    try {
                        p.setPrecio(Double.parseDouble(texto(request.getParameter("precio")).replace(",", ".")));
                    } catch (NumberFormatException e) {
                        response.sendRedirect("registroProductos.jsp?error=Precio+invalido");
                        return;
                    }
                    p.setCategoria(texto(request.getParameter("categoria")));
                    try {
                        p.setStock(Integer.parseInt(texto(request.getParameter("stock"))));
                    } catch (NumberFormatException e) {
                        response.sendRedirect("registroProductos.jsp?error=Stock+invalido");
                        return;
                    }
                    try {
                        gp.agregar(p);
                        response.sendRedirect("adminProductos.jsp?ok=Producto+agregado+exitosamente");
                    } catch (Exception ex) {
                        response.sendRedirect("registroProductos.jsp?error=" + encode(ex.getMessage()));
                    }
                    return;
                }

                case "editarProducto": {
                    if (!esAdmin(session)) {
                        response.sendRedirect("login.jsp?rol=admin");
                        return;
                    }
                    GestionarProductos gp = gestProductos(ctx);
                    Producto p = new Producto();
                    p.setId(texto(request.getParameter("id")));
                    p.setNombre(texto(request.getParameter("nombre")));
                    p.setDescripcion(texto(request.getParameter("descripcion")));
                    try {
                        p.setPrecio(Double.parseDouble(texto(request.getParameter("precio")).replace(",", ".")));
                    } catch (NumberFormatException e) {
                        response.sendRedirect("adminProductos.jsp?error=Precio+invalido");
                        return;
                    }
                    p.setCategoria(texto(request.getParameter("categoria")));
                    try {
                        p.setStock(Integer.parseInt(texto(request.getParameter("stock"))));
                    } catch (NumberFormatException e) {
                        response.sendRedirect("adminProductos.jsp?error=Stock+invalido");
                        return;
                    }
                    try {
                        gp.actualizar(p);
                        response.sendRedirect("adminProductos.jsp?ok=Catalogo+actualizado");
                    } catch (Exception ex) {
                        response.sendRedirect("adminProductos.jsp?error=" + encode(ex.getMessage()));
                    }
                    return;
                }

                case "eliminarProducto": {
                    if (!esAdmin(session)) {
                        response.sendRedirect("login.jsp?rol=admin");
                        return;
                    }
                    try {
                        gestProductos(ctx).eliminar(texto(request.getParameter("id")));
                        response.sendRedirect("adminProductos.jsp?ok=Producto+eliminado");
                    } catch (Exception ex) {
                        response.sendRedirect("adminProductos.jsp?error=" + encode(ex.getMessage()));
                    }
                    return;
                }

                // CARRITO 
                case "agregarCarrito": {
                    // Solo clientes pueden comprar
                    if (!"cliente".equals(session.getAttribute("rol"))) {
                        response.sendRedirect("login.jsp?rol=cliente&error=Inicia+sesion+como+cliente");
                        return;
                    }
                    String id = texto(request.getParameter("idProducto"));
                    Producto prod = gestProductos(ctx).buscarPorId(id);
                    if (prod != null) {
                        if (prod.getStock() <= 0) {
                            response.sendRedirect("catalogo.jsp?error=Stock+insuficiente");
                            return;
                        }
                        @SuppressWarnings("unchecked")
                        List<Map<String, String>> carrito =
                                (List<Map<String, String>>) session.getAttribute("carrito");
                        if (carrito == null) {
                            carrito = new ArrayList<>();
                            session.setAttribute("carrito", carrito);
                        }
                        Map<String, String> item = new LinkedHashMap<>();
                        item.put("id", prod.getId());
                        item.put("nombre", prod.getNombre());
                        item.put("descripcion", prod.getDescripcion());
                        item.put("precio", String.valueOf(prod.getPrecio()));
                        item.put("categoria", prod.getCategoria());
                        item.put("cantidad", "1");
                        carrito.add(item);
                    }
                    response.sendRedirect("catalogo.jsp?msg=Producto+agregado+al+carrito");
                    return;
                }

                case "quitarCarrito": {
                    @SuppressWarnings("unchecked")
                    List<Map<String, String>> carrito =
                            (List<Map<String, String>>) session.getAttribute("carrito");
                    if (carrito != null) {
                        try {
                            int i = Integer.parseInt(request.getParameter("index"));
                            if (i >= 0 && i < carrito.size()) {
                                carrito.remove(i);
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    response.sendRedirect("carrito.jsp");
                    return;
                }

                case "comprar": {
                    if (!"cliente".equals(session.getAttribute("rol"))) {
                        response.sendRedirect("login.jsp?rol=cliente");
                        return;
                    }
                    @SuppressWarnings("unchecked")
                    List<Map<String, String>> carrito =
                            (List<Map<String, String>>) session.getAttribute("carrito");
                    if (carrito == null || carrito.isEmpty()) {
                        response.sendRedirect("carrito.jsp?error=El+carrito+esta+vacio");
                        return;
                    }

                    String metodoPago = texto(request.getParameter("metodoPago"));
                    String detallePago = texto(request.getParameter("detallePago"));
                    if (metodoPago.isEmpty()) {
                        response.sendRedirect("carrito.jsp?error=Selecciona+un+metodo+de+pago");
                        return;
                    }
                    if (("Tarjeta".equalsIgnoreCase(metodoPago) || "PSE".equalsIgnoreCase(metodoPago)
                            || "Nequi".equalsIgnoreCase(metodoPago) || "Transferencia".equalsIgnoreCase(metodoPago))
                            && detallePago.isEmpty()) {
                        response.sendRedirect("carrito.jsp?error=Indica+el+detalle+del+pago");
                        return;
                    }

                    GestionarProductos gp = gestProductos(ctx);
                    GestionarPedidos gped = gestPedidos(ctx);

                    Pedido pedido = new Pedido();
                    pedido.setIdCliente(texto((String) session.getAttribute("idUsuario")));
                    pedido.setNombreCliente(texto((String) session.getAttribute("nombreUsuario")));
                    pedido.setMetodoPago(metodoPago);
                    pedido.setDetallePago(detallePago);
                    pedido.setEstado("Completado");

                    double total = 0;
                    try {
                        Map<String, Integer> pedidoPorProducto = new LinkedHashMap<>();
                        for (Map<String, String> item : carrito) {
                            int c = 1;
                            try {
                                c = Integer.parseInt(item.getOrDefault("cantidad", "1"));
                            } catch (NumberFormatException e) {
                                c = 1;
                            }
                            pedidoPorProducto.merge(item.get("id"), c, Integer::sum);
                        }
                        for (Map.Entry<String, Integer> e : pedidoPorProducto.entrySet()) {
                            Producto pr = gp.buscarPorId(e.getKey());
                            if (pr == null) {
                                throw new Exception("Producto no encontrado: " + e.getKey());
                            }
                            if (pr.getStock() < e.getValue()) {
                                throw new Exception("Stock insuficiente para: " + pr.getNombre()
                                        + " (disponible: " + pr.getStock() + ")");
                            }
                        }
                        for (Map<String, String> item : carrito) {
                            String idProd = item.get("id");
                            int cant = 1;
                            try {
                                cant = Integer.parseInt(item.getOrDefault("cantidad", "1"));
                            } catch (NumberFormatException e) {
                                cant = 1;
                            }
                            gp.descontarStock(idProd, cant);
                            double precio = Double.parseDouble(item.get("precio").replace(",", "."));
                            total += precio * cant;
                            pedido.addLinea(idProd + ":" + item.get("nombre") + ":" + precio + ":" + cant);
                        }
                        pedido.setTotal(total);
                        Pedido guardado = gped.crearPedido(pedido);
                        session.removeAttribute("carrito"); // Vacia el carrito
                        response.sendRedirect("carrito.jsp?msg=Compra+aprobada.+Tu+pedido+fue+procesado+correctamente");
                    } catch (Exception ex) {
                        response.sendRedirect("carrito.jsp?error=" + encode(ex.getMessage()));
                    }
                    return;
                }

                default:
                    response.sendRedirect("index_presentacion.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index_presentacion.jsp?error=" + encode("Error del sistema: " + e.getMessage()));
        }
    }

    
    private boolean esAdmin(HttpSession session) {
        return "admin".equals(session.getAttribute("rol"));
    }

    
    private String encode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return java.net.URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return s.replace(" ", "+");
        }
    }
}
