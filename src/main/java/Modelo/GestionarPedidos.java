package Modelo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class GestionarPedidos {

    private final Path archivoPedidos;
    private final Path archivoReporte;
    private final List<Pedido> lista = new ArrayList<>();

    public GestionarPedidos(String dataDir) throws IOException {
        Path dir = Paths.get(dataDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        this.archivoPedidos = dir.resolve("pedidos.txt");
        this.archivoReporte = dir.resolve("reporte.txt");
        cargar();
    }

    public synchronized void cargar() throws IOException {
        lista.clear();
        if (!Files.exists(archivoPedidos)) {
            Files.write(archivoPedidos,
                    "# id|idCliente|nombreCliente|metodoPago|detallePago|estado|total|lineas\n"
                            .getBytes(StandardCharsets.UTF_8));
            return;
        }
        try (BufferedReader br = Files.newBufferedReader(archivoPedidos, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                Pedido p = Pedido.fromFileLine(line);
                if (p != null) lista.add(p);
            }
        } catch (IOException e) {
            throw new IOException("Error al leer pedidos.txt: " + e.getMessage(), e);
        }
    }

    public synchronized void guardar() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(archivoPedidos, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write("# id|idCliente|nombreCliente|metodoPago|detallePago|estado|total|lineas");
            bw.newLine();
            for (Pedido p : lista) {
                bw.write(p.toFileLine());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error al guardar pedidos.txt: " + e.getMessage(), e);
        }
    }

    public List<Pedido> listarTodos() {
        return new ArrayList<>(lista);
    }

    public Pedido buscarPorId(String id) {
        for (Pedido p : lista) {
            if (id != null && id.equals(p.getId())) return p;
        }
        return null;
    }
    public synchronized Pedido crearPedido(Pedido pedido) throws Exception {
        if (pedido == null) throw new Exception("Pedido nulo");
        if (pedido.getLineasProducto() == null || pedido.getLineasProducto().isEmpty()) {
            throw new Exception("El carrito esta vacio");
        }
        if (isBlank(pedido.getMetodoPago())) {
            throw new Exception("Debe seleccionar un metodo de pago");
        }
        if (isBlank(pedido.getId())) pedido.setId(String.valueOf(siguienteId()));
        if (isBlank(pedido.getEstado())) pedido.setEstado("Completado");
        lista.add(pedido);
        guardar();
        generarFactura(pedido);
        return pedido;
    }

    public synchronized void generarFactura(Pedido pedido) throws Exception {
        if (pedido == null) throw new Exception("Pedido inexistente");
        if ("Cancelado".equalsIgnoreCase(pedido.getEstado())) {
            throw new Exception("No es posible generar la factura para este pedido (cancelado)");
        }

        StringBuilder sb = new StringBuilder();

        sb.append("           FACTURA \n");
        sb.append("Nro. pedido : ").append(pedido.getId()).append("\n");
        sb.append("Cliente     : ").append(pedido.getNombreCliente())
          .append(" (ID: ").append(pedido.getIdCliente()).append(")\n");
        sb.append("Estado      : ").append(pedido.getEstado()).append("\n");
        sb.append(String.format("%-30s %10s\n", "Producto", "Precio"));
        
        if (pedido.getLineasProducto() != null) {
            for (String L : pedido.getLineasProducto()) {
                String[] parts = L.split(":", -1);
                String nombre = parts.length > 1 ? parts[1] : L;
                String precio = parts.length > 2 ? parts[2] : "";
                String cant = parts.length > 3 ? parts[3] : "1";
                sb.append(String.format("%-30s %10s x%s\n", nombre, "$" + precio, cant));
            }
        }

        sb.append(String.format("TOTAL: $%.2f\n", pedido.getTotal()));
        sb.append("Metodo de pago : ").append(pedido.getMetodoPago()).append("\n");
        if (pedido.getDetallePago() != null && !pedido.getDetallePago().isEmpty()) {
            sb.append("Detalle pago   : ").append(pedido.getDetallePago()).append("\n");
        }

        try {
            Files.write(archivoReporte, sb.toString().getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IOException("Error al escribir reporte.txt: " + e.getMessage(), e);
        }
    }

    public String leerUltimoReporte() throws IOException {
        if (!Files.exists(archivoReporte)) {
            return "No hay reportes generados.";
        }
        return new String(Files.readAllBytes(archivoReporte), StandardCharsets.UTF_8);
    }

    private int siguienteId() {
        int max = 0;
        for (Pedido p : lista) {
            try {
                int n = Integer.parseInt(p.getId());
                if (n > max) max = n;
            } catch (NumberFormatException ignored) {}
        }
        return max + 1;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
