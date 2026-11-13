package Modelo;

public class Producto {

    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;
    private int stock;

    public Producto() {}

    public Producto(String id, String nombre, String descripcion, double precio, String categoria, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String toFileLine() {
        return safe(id) + "|" + safe(nombre) + "|" + safe(descripcion)
                + "|" + precio + "|" + safe(categoria) + "|" + stock;
    }

    public static Producto fromFileLine(String line) {
        if (line == null || line.trim().isEmpty() || line.startsWith("#")) {
            return null;
        }
        String[] p = line.split("\\|", -1);
        if (p.length < 4) {
            return null;
        }
        Producto prod = new Producto();
        prod.setId(p[0]);
        prod.setNombre(p[1]);
        prod.setDescripcion(p[2]);
        try {
            prod.setPrecio(Double.parseDouble(p[3].replace(",", ".")));
        } catch (NumberFormatException e) {
            prod.setPrecio(0);
        }
        prod.setCategoria(p.length > 4 ? p[4] : "General");
        try {
            prod.setStock(p.length > 5 ? Integer.parseInt(p[5]) : 0);
        } catch (NumberFormatException e) {
            prod.setStock(0);
        }
        return prod;
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace("|", "/").replace("\n", " ");
    }

    @Override
    public String toString() {
        return "Producto{id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", stock=" + stock + "}";
    }
}
