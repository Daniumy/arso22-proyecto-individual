package eventos;

import java.time.LocalDateTime;

public class Valoracion {

	private String email;
	private LocalDateTime fechaRegistro;
	private double calificacion;
	private String comentario;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public double getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(double d) {
		this.calificacion = d;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

}
