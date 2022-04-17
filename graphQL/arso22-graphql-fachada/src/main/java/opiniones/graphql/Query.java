package opiniones.graphql;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import opiniones.model.Opinion;
import opiniones.repositorio.EntidadNoEncontrada;
import opiniones.repositorio.RepositorioException;
import opiniones.servicio.ServicioOpiniones;

public class Query implements GraphQLRootResolver {
	//Recuperar la opinión de una URL.
	public Opinion getOpinion(String url) throws RepositorioException, EntidadNoEncontrada {
		return ServicioOpiniones.getInstancia().getByUrl(url);
	}
}
