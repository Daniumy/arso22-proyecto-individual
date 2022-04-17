package opiniones.graphql;

import java.util.LinkedHashMap;
import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import graphql.schema.GraphQLInputObjectType;
import opiniones.model.Opinion;
import opiniones.model.Valoracion;
import opiniones.repositorio.EntidadNoEncontrada;
import opiniones.repositorio.RepositorioException;
import opiniones.servicio.ServicioOpiniones;

public class Mutation implements GraphQLRootResolver {
	// Registrar una URL para ser valorada (crea una opinión).
	public Opinion crearOpinion(String url) throws RepositorioException {

		Opinion opinion = new Opinion();
		opinion.setUrl(url);

		String id = ServicioOpiniones.getInstancia().create(opinion);

		opinion.setIdentificador(id);

		return opinion;
	}

	public Valoracion anadirValoracion(String url, LinkedHashMap<String, Object> valoracionInput) throws RepositorioException, EntidadNoEncontrada {
		// Añadir una valoración para una URL. Si un usuario registra una segunda
		// valoración para una misma URL, ésta reemplazará a la primera.
		Valoracion valoracion = new Valoracion();
		valoracion.setEmail(valoracionInput.get("email").toString());
		valoracion.setCalificacion((Integer) valoracionInput.get("calificacion"));
		
		if (valoracionInput.containsKey("comentario")) valoracion.setComentario(valoracionInput.get("comentario").toString());
		
		ServicioOpiniones.getInstancia().anadirValoracion(url, valoracion);
		
		return valoracion;
	}
	
	public boolean eliminarOpinion(String url) throws RepositorioException, EntidadNoEncontrada {
		ServicioOpiniones.getInstancia().removeUrl(url);
		return true;
	}
}
