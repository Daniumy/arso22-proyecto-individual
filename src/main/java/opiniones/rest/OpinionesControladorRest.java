package opiniones.rest;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import opiniones.model.Opinion;
import opiniones.model.Valoracion;
import opiniones.repositorio.EntidadNoEncontrada;
import opiniones.repositorio.RepositorioException;
import opiniones.servicio.IServicioOpiniones;
import opiniones.servicio.ServicioOpiniones;

@Path("opiniones")
public class OpinionesControladorRest {

	private IServicioOpiniones servicio = ServicioOpiniones.getInstancia();

	@Context
	private UriInfo uriInfo;

	// String create(Opinion opinion) throws RepositorioException;

	// curl -i -X POST -H "Content-Type: application/json" -d @testfiles/1.json
	// http://localhost:8080/api/opiniones/

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Opinion opinion) throws Exception {

		String id = servicio.create(opinion);
		System.out.println(id);
		URI uri = uriInfo.getAbsolutePathBuilder().path(id).build();

		return Response.created(uri).build();
	}

	// Eliminar una URL del servicio (elimina una opinión y sus valoraciones).
	// curl -i -X DELETE
	// http://localhost:8080/api/opiniones/https://www.urldeprueba.com
	// public void removeUrl(String url) throws RepositorioException,
	// EntidadNoEncontrada
	@DELETE
	@Path("{url: .*}")
	public Response removeUrl(@PathParam("url") String url) throws Exception {
		System.out.println(url);
		servicio.removeUrl(url);

		return Response.noContent().build();
	}

	// Recuperar la opinión de una URL.
	// public Opinion getByUrl(String url) throws RepositorioException,
	// EntidadNoEncontrada
	// curl http://localhost:8080/api/opiniones/https://www.urldeprueba.com

	@GET
	@Path("{url: .*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByUrl(@PathParam("url") String url) throws Exception {
		Opinion opinion = servicio.getByUrl(url);
		return Response.status(Status.OK).entity(opinion).build();
	}

	
	//curl -i -X POST -H "Content-Type: application/json" -d @testfiles/valoracion.json http://localhost:8080/api/opiniones/https://www.urldeprueba.com
	// public void anadirValoracion(String url, Valoracion valoracion) throws
	// RepositorioException, EntidadNoEncontrada
	
	@POST
	@Path("{url: .*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response anadirValoracion(@PathParam("url") String url, Valoracion valoracion) throws Exception {

		servicio.anadirValoracion(url, valoracion);

		return Response.status(Response.Status.NO_CONTENT).build();
	}

	
	
	
	
	
	
	
	
	
	
	// Opinion getById(String id) throws RepositorioException, EntidadNoEncontrada;

	// http://localhost:8080/api/opiniones/624cd89f5d9ce11ef003885f
//	@GET
//	@Path("{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getById(@PathParam("id") String id) throws Exception {
//		Opinion opinion = servicio.getById(id);
//		System.out.println(id);
//		return Response.status(Status.OK).entity(opinion).build();
//	}

//	void remove(String id) throws RepositorioException, EntidadNoEncontrada;

//	@DELETE
//	@Path("{id}")
//	public Response remove(String id) throws Exception {
//		
//		servicio.remove(id);
//		
//		return Response.noContent().build();
//	}

}
