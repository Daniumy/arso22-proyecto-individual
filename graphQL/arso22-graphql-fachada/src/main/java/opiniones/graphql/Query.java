package opiniones.graphql;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import opiniones.model.Opinion;
import opiniones.repositorio.EntidadNoEncontrada;
import opiniones.repositorio.RepositorioException;
import opiniones.servicio.ServicioOpiniones;

public class Query implements GraphQLRootResolver {
	public Opinion getOpinion(String url) throws RepositorioException, EntidadNoEncontrada {
		System.out.println("\n\n\n\nAMAI\n\n\n\n");
		return ServicioOpiniones.getInstancia().getByUrl(url);
	}
}
