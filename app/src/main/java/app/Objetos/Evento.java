package app.Objetos;

public class Evento {

    private int id;
    private String titulo;
    private String ruta_imagen;
    private String descripcion;
    private String fecha;
    private String hora;
    private String precio;
    private String hayInscripcion;
    private boolean esPublico;
    private float valoracionEvento;
    private float valoracionUsuario;

    public Evento(int id, String titulo, String ruta_imagen, String descripcion, String fecha, String hayInscripcion,
                  String hora, String precio, boolean esPublico, float valoracionEvento, float valoracionUsuario){
        this.id = id;
        this.titulo = titulo;
        this.ruta_imagen = ruta_imagen;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hayInscripcion = hayInscripcion;
        this.esPublico = esPublico;
        this.hora = hora;
        this.precio = precio;
        this.valoracionEvento = valoracionEvento;
        this.valoracionUsuario = valoracionUsuario;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getRuta_imagen() {
        return ruta_imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHayInscripcion() {
        return hayInscripcion;
    }

    public String getHora() {
        return hora;
    }

    public String getPrecio() {
        return precio;
    }

    public boolean isEsPublico() {
        return esPublico;
    }

    public float getValoracionEvento() {
        return valoracionEvento;
    }

    public float getValoracionUsuario() {
        return valoracionUsuario;
    }
}
