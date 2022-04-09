package opiniones.graphql;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import opiniones.model.Opinion;
import opiniones.model.Valoracion;
import opiniones.repositorio.RepositorioException;
import opiniones.servicio.ServicioOpiniones;

public class Mutation implements GraphQLRootResolver{

	 public Opinion crearOpinion(String url, 
	    		List<Valoracion> valoraciones) throws RepositorioException {
	        
	    	Opinion opinion = new Opinion();
	    	opinion.setUrl(url);
	    	
	    	for (Valoracion valoracion : valoraciones) {
	    		opinion.getValoraciones().add(valoracion);    		
	    	}
	    	
	    	String id = ServicioOpiniones.getInstancia().create(opinion);
	    
	    	opinion.setIdentificador(id);
	    	
	    	return opinion;
	    	
	    }
}
