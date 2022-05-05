package opiniones.eventos;

import opiniones.model.Valoracion;

public class EventoValoracionCreada {
	private int numValoraciones;
	private String url;
	private double calificacionMedia;
	private Valoracion valoracion;
	public int getNumValoraciones() {
		return numValoraciones;
	}
	public void setNumValoraciones(int numValoraciones) {
		this.numValoraciones = numValoraciones;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public double getCalificacionMedia() {
		return calificacionMedia;
	}
	public void setCalificacionMedia(double calificacionMedia) {
		this.calificacionMedia = calificacionMedia;
	}
	public Valoracion getValoracion() {
		return valoracion;
	}
	public void setValoracion(Valoracion valoracion) {
		this.valoracion = valoracion;
	}
	
	
}
