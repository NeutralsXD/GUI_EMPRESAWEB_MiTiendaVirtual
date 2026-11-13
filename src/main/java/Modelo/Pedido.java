package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private String id;
    private String idCliente;
    private String nombreCliente;
    private String metodoPago;
    private String detallePago;
    private String estado;
    private double total;
    private List<String> lineasProducto;

    public Pedido() {
        this.lineasProducto = new ArrayList<>();
        this.estado = "Pendiente";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getDetallePago() { return detallePago; }
    public void setDetallePago(String detallePago) { this.detallePago = detallePago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public List<String> getLineasProducto() { return lineasProducto; }
    public void setLineasProducto(List<String> lineasProducto) { this.lineasProducto = lineasProducto; }

    public void addLinea(String linea) {
        if (lineasProducto == null) {
            lineasProducto = new ArrayList<>();
        }
        lineasProducto.add(linea);
    }

    public String toFileLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(s(id)).append("|").append(s(idCliente)).append("|").append(s(nombreCliente))
          .append("|").append(s(metodoPago)).append("|").append(s(detallePago))
          .append("|").append(s(estado)).append("|").append(total).append("|");
        if (lineasProducto != null) {
            for (int i = 0; i < lineasProducto.size(); i++) {
                if (i > 0) sb.append(";");
                sb.append(s(lineasProducto.get(i)));
            }
        }
        return sb.toString();
    }

    public static Pedido fromFileLine(String line) {
        if (line == null || line.trim().isEmpty() || line.startsWith("#")) {
            return null;
        }
        String[] p = line.split("\\|", -1);
        if (p.length < 7) {
            return null;
        }
        Pedido ped = new Pedido();
        ped.setId(p[0]);
        ped.setIdCliente(p[1]);
        ped.setNombreCliente(p[2]);
        ped.setMetodoPago(p[3]);
        ped.setDetallePago(p[4]);
        ped.setEstado(p[5]);
        try {
            ped.setTotal(Double.parseDouble(p[6].replace(",", ".")));
        } catch (NumberFormatException e) {
            ped.setTotal(0);
        }
        if (p.length > 7 && !p[7].isEmpty()) {
            for (String L : p[7].split(";")) {
                if (!L.isEmpty()) ped.addLinea(L);
            }
        }
        return ped;
    }

    private static String s(String v) {
        return v == null ? "" : v.replace("|", "/").replace(";", ",").replace("\n", " ");
    }

    @Override
    public String toString() {
        return "Pedido{id=" + id + ", cliente=" + nombreCliente + ", total=" + total + ", pago=" + metodoPago + "}";
    }
}
