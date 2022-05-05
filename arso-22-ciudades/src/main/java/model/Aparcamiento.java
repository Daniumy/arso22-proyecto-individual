package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Aparcamiento {

    private String ciudad;
    private String direccion;
    private String foto;
    private double longitud;
    private double latitud;
    private int numValoraciones;
    private double calificacionMedia;
    private String url;
    public Aparcamiento() {
    }

    public Aparcamiento(String ciudad, String direccion, double longitud, double latitud, int numValoraciones, double calificacionMedia, String url) {
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.longitud = longitud;
        this.latitud = latitud;
        this.calificacionMedia = calificacionMedia;
        this.numValoraciones = numValoraciones;
        this.url = url;
    }
    
    
    public int getNumValoraciones() {
		return numValoraciones;
	}

	public void setNumValoraciones(int numValoraciones) {
		this.numValoraciones = numValoraciones;
	}

	public double getCalificacionMedia() {
		return calificacionMedia;
	}

	public void setCalificacionMedia(double calificacionMedia) {
		this.calificacionMedia = calificacionMedia;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Aparcamiento{" +
                "ciudad='" + ciudad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", foto='" + foto + '\'' +
                ", longitud=" + longitud +
                ", latitud=" + latitud +
                '}';
    }
}
