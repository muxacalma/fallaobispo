package app.Objetos;

public class Usuario {

    private String email, nombreSimple, nombreCompleto;
    int esAdmin, crearEventos, eliminarEventos, publicarNoticias, eliminarNoticias, crearTorneos, eliminarTorneos, gestionarUsuarios;

    public Usuario(String email, String nombreSimple, String nombreCompleto, int esAdmin, int crearEventos, int eliminarEventos, int publicarNoticias, int eliminarNoticias,
                   int crearTorneos, int eliminarTorneos, int gestionarUsuarios){
        this.email = email;
        this.nombreSimple = nombreSimple;
        this.nombreCompleto = nombreCompleto;
        this.esAdmin = esAdmin;
        this.crearEventos = crearEventos;
        this.eliminarEventos = eliminarEventos;
        this.publicarNoticias = publicarNoticias;
        this.eliminarNoticias = eliminarNoticias;
        this.crearTorneos = crearTorneos;
        this.eliminarTorneos = eliminarTorneos;
        this.gestionarUsuarios = gestionarUsuarios;
    }

    public String getEmail() {
        return email;
    }

    public String getNombreSimple() {
        return nombreSimple;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public int getEsAdmin() {
        return esAdmin;
    }

    public int getCrearEventos() {
        return crearEventos;
    }

    public int getEliminarEventos() {
        return eliminarEventos;
    }

    public int getPublicarNoticias() {
        return publicarNoticias;
    }

    public int getEliminarNoticias() {
        return eliminarNoticias;
    }

    public int getCrearTorneos() {
        return crearTorneos;
    }

    public int getEliminarTorneos() {
        return eliminarTorneos;
    }

    public int getGestionarUsuarios() {
        return gestionarUsuarios;
    }
}
