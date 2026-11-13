package Modelo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class GestionarProductos {

    private final Path archivo;
    private final List<Producto> lista = new ArrayList<>();

    public GestionarProductos(String dataDir) throws IOException {
        Path dir = Paths.get(dataDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        this.archivo = dir.resolve("items.txt");
        cargar();
    }

    public synchronized void cargar() throws IOException {
        lista.clear();
        if (!Files.exists(archivo)) {
            Files.write(archivo,
                    "# id|nombre|descripcion|precio|categoria|stock\n"
                            .getBytes(StandardCharsets.UTF_8));
            return;
        }
        try (BufferedReader br = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                Producto p = Producto.fromFileLine(line);
                if (p != null) lista.add(p);
            }
        } catch (IOException e) {
            throw new IOException("Error al leer items.txt: " + e.getMessage(), e);
        }
    }

    public synchronized void guardar() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write("# id|nombre|descripcion|precio|categoria|stock");
            bw.newLine();
            for (Producto p : lista) {
                bw.write(p.toFileLine());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error al guardar items.txt: " + e.getMessage(), e);
        }
    }

    public List<Producto> listarTodos() {
        return new ArrayList<>(lista);
    }

    public List<Producto> filtrar(String campo, String valor) {
        List<Producto> out = new ArrayList<>();
        if (valor == null || valor.trim().isEmpty()) {
            return listarTodos();
        }
        String v = valor.trim().toLowerCase();
        for (Producto p : lista) {
            boolean match = false;
            if ("nombre".equalsIgnoreCase(campo) && p.getNombre() != null && p.getNombre().toLowerCase().contains(v)) {
                match = true;
            } else if ("categoria".equalsIgnoreCase(campo) && p.getCategoria() != null && p.getCategoria().toLowerCase().contains(v)) {
                match = true;
            } else if ("id".equalsIgnoreCase(campo) && p.getId() != null && p.getId().equalsIgnoreCase(v)) {
                match = true;
            } else if (campo == null || campo.isEmpty()) {
                if ((p.getNombre() != null && p.getNombre().toLowerCase().contains(v))
                        || (p.getCategoria() != null && p.getCategoria().toLowerCase().contains(v))
                        || (p.getDescripcion() != null && p.getDescripcion().toLowerCase().contains(v))) {
                    match = true;
                }
            }
            if (match) out.add(p);
        }
        return out;
    }

    public Producto buscarPorId(String id) {
        for (Producto p : lista) {
            if (id != null && id.equals(p.getId())) return p;
        }
        return null;
    }

    public synchronized void agregar(Producto p) throws Exception {
        if (p == null) throw new Exception("Producto nulo");
        if (isBlank(p.getNombre())) throw new Exception("El nombre del producto es obligatorio");
        if (p.getPrecio() < 0) throw new Exception("El precio no puede ser negativo");
        if (p.getStock() < 0) throw new Exception("El stock no puede ser negativo");
        if (isBlank(p.getId())) p.setId(String.valueOf(siguienteId()));
        if (isBlank(p.getCategoria())) p.setCategoria("General");
        lista.add(p);
        guardar();
    }

    public synchronized void actualizar(Producto p) throws Exception {
        if (p == null || isBlank(p.getId())) throw new Exception("ID de producto requerido");
        Producto actual = buscarPorId(p.getId());
        if (actual == null) throw new Exception("Producto no encontrado");
        if (p.getPrecio() < 0) throw new Exception("El precio no puede ser negativo");
        if (p.getStock() < 0) throw new Exception("El stock no puede ser negativo");
        actual.setNombre(p.getNombre());
        actual.setDescripcion(p.getDescripcion());
        actual.setPrecio(p.getPrecio());
        actual.setCategoria(p.getCategoria());
        actual.setStock(p.getStock());
        guardar();
    }

    public synchronized void eliminar(String id) throws Exception {
        Producto p = buscarPorId(id);
        if (p == null) throw new Exception("Producto no encontrado");
        lista.remove(p);
        guardar();
    }

    public synchronized void descontarStock(String idProducto, int cantidad) throws Exception {
        Producto p = buscarPorId(idProducto);
        if (p == null) throw new Exception("Producto no encontrado: " + idProducto);
        if (p.getStock() < cantidad) {
            throw new Exception("Stock insuficiente para: " + p.getNombre()
                    + " (disponible: " + p.getStock() + ")");
        }
        p.setStock(p.getStock() - cantidad);
        guardar();
    }

    private int siguienteId() {
        int max = 0;
        for (Producto p : lista) {
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
