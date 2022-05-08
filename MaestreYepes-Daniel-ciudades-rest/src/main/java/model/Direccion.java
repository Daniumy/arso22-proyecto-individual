package model;

import javax.json.bind.annotation.JsonbProperty;

public class Direccion {

    private String calle;
    private int numero;
    private String localidad;
    @JsonbProperty("codigoPostal")
    private String codigoPostal;
    private double lat;
    private double lng;

    public Direccion() {
    }

    public Direccion(String calle, int numero, String localidad, String codigoPostal, double lat, double lng) {
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.codigoPostal = codigoPostal;
        this.lat = lat;
        this.lng = lng;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "calle='" + calle + '\'' +
                ", numero=" + numero +
                ", localidad='" + localidad + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}