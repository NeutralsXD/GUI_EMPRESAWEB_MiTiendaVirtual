package Modelo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class GestionarUsuarios {

    private final Path archivo;
    private final List<Usuario> lista = new ArrayList<>();

    public GestionarUsuarios(String dataDir) throws IOException {
        Path dir = Paths.get(dataDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        this.archivo = dir.resolve("usuarios.txt");
        cargar();
        asegurarAdmin();
    }

    private void asegurarAdmin() throws IOException {
        boolean hayAdmin = false;
        for (Usuario u : lista) {
            if ("admin".equalsIgnoreCase(u.getRol())) {
                hayAdmin = true;
                break;
            }
        }
        if (!hayAdmin) {
            Usuario admin = new Usuario("1", "Administrador", "admin@tienda.com",
                    "admin", "admin", "0000000000", "N/A", "admin");
            lista.add(admin);
            guardar();
        }
    }

    public synchronized void cargar() throws IOException {
        lista.clear();
        if (!Files.exists(archivo)) {
            Files.write(archivo,
                    "# id|nombre|email|usuario|contrasena|telefono|direccion|rol\n"
                            .getBytes(StandardCharsets.UTF_8));
            return;
        }
        try (BufferedReader br = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                Usuario u = Usuario.fromFileLine(line);
                if (u != null) lista.add(u);
            }
        } catch (IOException e) {
            throw new IOException("Error al leer usuarios.txt: " + e.getMessage(), e);
        }
    }

    public synchronized void guardar() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write("# id|nombre|email|usuario|contrasena|telefono|direccion|rol");
            bw.newLine();
            for (Usuario u : lista) {
                bw.write(u.toFileLine());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error al guardar usuarios.txt: " + e.getMessage(), e);
        }
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(lista);
    }

    public List<Usuario> filtrar(String campo, String valor) {
        List<Usuario> out = new ArrayList<>();
        if (valor == null || valor.trim().isEmpty()) {
            return listarTodos();
        }
        String v = valor.trim().toLowerCase();
        for (Usuario u : lista) {
            boolean match = false;
            if ("nombre".equalsIgnoreCase(campo) && u.getNombre() != null && u.getNombre().toLowerCase().contains(v)) {
                match = true;
            } else if ("usuario".equalsIgnoreCase(campo) && u.getUsuario() != null && u.getUsuario().toLowerCase().contains(v)) {
                match = true;
            } else if ("email".equalsIgnoreCase(campo) && u.getEmail() != null && u.getEmail().toLowerCase().contains(v)) {
                match = true;
            } else if ("id".equalsIgnoreCase(campo) && u.getId() != null && u.getId().equalsIgnoreCase(v)) {
                match = true;
            } else if (campo == null || campo.isEmpty()) {
                if ((u.getNombre() != null && u.getNombre().toLowerCase().contains(v))
                        || (u.getUsuario() != null && u.getUsuario().toLowerCase().contains(v))
                        || (u.getEmail() != null && u.getEmail().toLowerCase().contains(v))) {
                    match = true;
                }
            }
            if (match) out.add(u);
        }
        return out;
    }

    public Usuario buscarPorId(String id) {
        for (Usuario u : lista) {
            if (id != null && id.equals(u.getId())) return u;
        }
        return null;
    }

    public Usuario buscarPorUsuario(String user) {
        for (Usuario u : lista) {
            if (user != null && user.equals(u.getUsuario())) return u;
        }
        return null;
    }

    public Usuario buscarPorEmail(String email) {
        for (Usuario u : lista) {
            if (email != null && email.equalsIgnoreCase(u.getEmail())) return u;
        }
        return null;
    }

    public synchronized void agregar(Usuario u) throws Exception {
        if (u == null) throw new Exception("Usuario nulo");
        if (isBlank(u.getNombre()) || isBlank(u.getEmail())
                || isBlank(u.getUsuario()) || isBlank(u.getContrasena())) {
            throw new Exception("Faltan campos obligatorios (nombre, email, usuario, contrasena)");
        }
        if (buscarPorEmail(u.getEmail()) != null) throw new Exception("El correo ya esta registrado");
        if (buscarPorUsuario(u.getUsuario()) != null) throw new Exception("El nombre de usuario ya existe");
        if (isBlank(u.getId())) u.setId(String.valueOf(siguienteId()));
        if (isBlank(u.getRol())) u.setRol("cliente");
        lista.add(u);
        guardar();
    }

    public synchronized void actualizar(Usuario u) throws Exception {
        if (u == null || isBlank(u.getId())) throw new Exception("ID de usuario requerido");
        Usuario actual = buscarPorId(u.getId());
        if (actual == null) throw new Exception("Cliente no encontrado");
        Usuario porEmail = buscarPorEmail(u.getEmail());
        if (porEmail != null && !porEmail.getId().equals(u.getId())) {
            throw new Exception("El correo nuevo ya esta en uso");
        }
        actual.setNombre(u.getNombre());
        actual.setEmail(u.getEmail());
        actual.setUsuario(u.getUsuario());
        if (!isBlank(u.getContrasena())) actual.setContrasena(u.getContrasena());
        actual.setTelefono(u.getTelefono());
        actual.setDireccion(u.getDireccion());
        if (!isBlank(u.getRol())) actual.setRol(u.getRol());
        guardar();
    }

    public synchronized void eliminar(String id) throws Exception {
        Usuario u = buscarPorId(id);
        if (u == null) throw new Exception("Cliente no encontrado");
        if ("admin".equalsIgnoreCase(u.getRol()) && "admin".equalsIgnoreCase(u.getUsuario())) {
            throw new Exception("No se puede eliminar el administrador principal");
        }
        lista.remove(u);
        guardar();
    }

    private int siguienteId() {
        int max = 0;
        for (Usuario u : lista) {
            try {
                int n = Integer.parseInt(u.getId());
                if (n > max) max = n;
            } catch (NumberFormatException ignored) {}
        }
        return max + 1;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
