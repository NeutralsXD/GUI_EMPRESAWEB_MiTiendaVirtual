package Modelo;

public class Usuario {

    private String id;
    private String nombre;
    private String email;
    private String usuario;
    private String contrasena;
    private String telefono;
    private String direccion;
    private String rol; 
    public Usuario() {
        this.rol = "cliente";
    }

    public Usuario(String id, String nombre, String email, String usuario,
                   String contrasena, String telefono, String direccion, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = (rol == null || rol.isEmpty()) ? "cliente" : rol;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String toFileLine() {
        return join(id) + "|" + join(nombre) + "|" + join(email) + "|" + join(usuario)
                + "|" + join(contrasena) + "|" + join(telefono) + "|" + join(direccion) + "|" + join(rol);
    }

    public static Usuario fromFileLine(String line) {
        if (line == null || line.trim().isEmpty() || line.startsWith("#")) {
            return null;
        }
        String[] p = line.split("\\|", -1);
        if (p.length < 5) {
            return null;
        }
        Usuario u = new Usuario();
        u.setId(p[0]);
        u.setNombre(p[1]);
        u.setEmail(p[2]);
        u.setUsuario(p[3]);
        u.setContrasena(p[4]);
        u.setTelefono(p.length > 5 ? p[5] : "");
        u.setDireccion(p.length > 6 ? p[6] : "");
        u.setRol(p.length > 7 ? p[7] : "cliente");
        return u;
    }

    private static String join(String s) {
        return s == null ? "" : s.replace("|", "/").replace("\n", " ");
    }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", usuario=" + usuario + ", rol=" + rol + "}";
    }
}
