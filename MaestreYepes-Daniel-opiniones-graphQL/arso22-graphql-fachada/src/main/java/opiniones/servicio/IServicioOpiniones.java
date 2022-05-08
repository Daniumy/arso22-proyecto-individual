package opiniones.servicio;

import javax.jws.WebService;

import opiniones.model.Opinion;
import opiniones.model.Valoracion;
import opiniones.repositorio.EntidadNoEncontrada;
import opiniones.repositorio.RepositorioException;

@WebService
public interface IServicioOpiniones {
	String create(Opinion opinion) throws RepositorioException;

	Opinion getById(String id) throws RepositorioException, EntidadNoEncontrada;
	
	//Recuperar la opinión de una URL.
	Opinion getByUrl(String url) throws RepositorioException, EntidadNoEncontrada;
	
	void remove(String id) throws RepositorioException, EntidadNoEncontrada;
	//Añadir una valoración para una URL. Si un usuario registra una segunda valoración para una misma URL, ésta reemplazará a la primera.
	void anadirValoracion(String url, Valoracion valoracion) throws RepositorioException, EntidadNoEncontrada;
	
	void removeAll() throws RepositorioException, EntidadNoEncontrada;
	//Eliminar una URL del servicio (elimina una opinión y sus valoraciones).
	void removeUrl(String url) throws RepositorioException, EntidadNoEncontrada;
}
