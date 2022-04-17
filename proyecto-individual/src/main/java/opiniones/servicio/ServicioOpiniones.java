package opiniones.servicio;

import javax.jws.WebService;

import opiniones.model.Opinion;
import opiniones.model.Valoracion;
import opiniones.repositorio.EntidadNoEncontrada;
import opiniones.repositorio.FactoriaRepositorioOpiniones;
import opiniones.repositorio.RepositorioException;
import opiniones.repositorio.RepositorioOpiniones;

@WebService(endpointInterface = "opiniones.servicio.IServicioOpiniones")
public class ServicioOpiniones implements IServicioOpiniones {

	private RepositorioOpiniones repositorio = FactoriaRepositorioOpiniones.getRepositorio();

	/** Patrón Singleton **/

	private static ServicioOpiniones instancia;

	private ServicioOpiniones() {

	}

	public static ServicioOpiniones getInstancia() {

		if (instancia == null)
			instancia = new ServicioOpiniones();

		return instancia;
	}

	// Registrar una URL para ser valorada (crea una opinión).
	@Override
	public String create(Opinion opinion) throws RepositorioException {
		// Control de integridad de los datos

		// 1. Campos obligatorios
		if (opinion.getUrl() == null || opinion.getUrl().isEmpty())
			throw new IllegalArgumentException("URL: no debe ser nulo ni vacio");

		if (repositorio.isUrlRepetida(opinion.getUrl()))
			throw new IllegalArgumentException("URL: solo puede haber una instancia por URL");

		if (opinion.getValoraciones() == null)
			throw new IllegalArgumentException("valoraciones: no debe ser una coleccion nula");

		if (opinion.getNumValoraciones() == 0)
			throw new IllegalArgumentException("valoraciones: no debe ser una coleccion que no tiene valoraciones");

		if (opinion.getCalificacionMedia() == 0)
			throw new IllegalArgumentException("valoraciones: no debe ser una coleccion cuya valoracion media es 0");

		for (Valoracion valoracion : opinion.getValoraciones()) {
			if (valoracion.getEmail() == null || valoracion.getEmail().isEmpty())
				throw new IllegalArgumentException("valoracion, email: no debe ser nulo ni vacio");

			if (valoracion.getCalificacion() < 1 || valoracion.getCalificacion() > 5)
				throw new IllegalArgumentException("calificacion: debe de estar entre 1 y 5");
			if (valoracion.getFechaRegistro() == null)
				throw new IllegalArgumentException("fechaRegistro: no debe ser nula");
		}

		String id = repositorio.add(opinion);

		return id;
	}
	
//Añadir una valoración para una URL. Si un usuario registra una segunda valoración para una misma URL, ésta reemplazará a la primera.
	@Override
	public void anadirValoracion(String url, Valoracion valoracion) throws RepositorioException, EntidadNoEncontrada {
		Opinion opinion = getByUrl(url);
		String emailNuevaValoracion = valoracion.getEmail();
		for (Valoracion valoracionaux: opinion.getValoraciones()) {
			if (valoracionaux.getEmail().equals(emailNuevaValoracion)) {
				valoracionaux.setCalificacion(valoracion.getCalificacion());
				valoracionaux.setComentario(valoracion.getComentario());
				valoracionaux.setFechaRegistro(valoracion.getFechaRegistro());
				repositorio.update(opinion);
				System.out.println("modificado uno");
				return;
			}
		}
		opinion.getValoraciones().add(valoracion);
		System.out.println("añadido uno extra");
		repositorio.update(opinion);
	}
	
	@Override
	public Opinion getByUrl(String url) throws RepositorioException, EntidadNoEncontrada {
		return repositorio.getByUrl(url);
	}

	@Override
	public Opinion getById(String id) throws RepositorioException, EntidadNoEncontrada {
		return repositorio.getById(id);
	}

	@Override
	public void remove(String id) throws RepositorioException, EntidadNoEncontrada {
		Opinion opinion = repositorio.getById(id);

		repositorio.delete(opinion);

	}
	@Override
	public void removeAll() throws RepositorioException, EntidadNoEncontrada {
		for (Opinion opinion: repositorio.getAll()) {
			remove(opinion.getIdentificador());
		}
		
	}
	@Override
	public void removeUrl(String url) throws RepositorioException, EntidadNoEncontrada {
		Opinion opinion = getByUrl(url);
		
		repositorio.delete(opinion);
	}

}
