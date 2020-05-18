package app.Objetos;

public class Noticia {

    private int id;
    private String titulo, descripcion, rutaImagen, fecha;

    public Noticia(int id, String titulo, String descripcion, String rutaImagen, String fecha){
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public String getFecha() {
        return fecha;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
