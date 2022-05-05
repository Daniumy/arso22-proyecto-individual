package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SitioInteres {

    private String id;
    private String nombre;
    private String descripcion;
    private String photo;
    private int rank;
    private Direccion direccion;

    public SitioInteres() {
    }

    public SitioInteres(String id, String nombre, String descripcion, String photo, Direccion direccion, int rank) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.photo = photo;
        this.direccion = direccion;
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "SitioInteres{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", rank=" + rank +
                ", direccion=" + direccion.toString() +
                '}';
    }
}
