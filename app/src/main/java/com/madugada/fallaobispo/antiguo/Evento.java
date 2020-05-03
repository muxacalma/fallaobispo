package com.madugada.fallaobispo.antiguo;

public class Evento {

    private int id;
    private String titulo, ruta_imagen, descripcion, fecha;
    private boolean hayInscripcion;

    public Evento(int id, String titulo, String ruta_imagen, String descripcion, String fecha, boolean hayInscripcion){
        this.id = id;
        this.titulo = titulo;
        this.ruta_imagen = ruta_imagen;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hayInscripcion = hayInscripcion;
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

    public boolean isHayInscripcion() {
        return hayInscripcion;
    }
}
