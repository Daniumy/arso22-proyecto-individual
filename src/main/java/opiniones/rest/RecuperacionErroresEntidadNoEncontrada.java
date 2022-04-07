package opiniones.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import opiniones.repositorio.EntidadNoEncontrada;


@Provider
public class RecuperacionErroresEntidadNoEncontrada implements ExceptionMapper<EntidadNoEncontrada> {
	@Override
	public Response toResponse(EntidadNoEncontrada arg0) {
		return Response.status(Response.Status.NOT_FOUND).entity(arg0.getMessage()).build();
	}
}