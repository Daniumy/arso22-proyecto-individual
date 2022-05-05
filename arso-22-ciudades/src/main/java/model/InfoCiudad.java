package model;

public class InfoCiudad {

    private int id;
    private String nombre;
    protected String codigoPostal;

    public InfoCiudad(int id, String nombre, String codigoPostal) {
        this.id = id;
        this.nombre = nombre;
        this.codigoPostal = codigoPostal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @Override
    public String toString() {
        return "InfoCiudad{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                '}';
    }
}
