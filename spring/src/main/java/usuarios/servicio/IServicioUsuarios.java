package usuarios.servicio;

import javax.jws.WebService;

import usuarios.model.Usuario;
import usuarios.repositorio.EntidadNoEncontrada;
import usuarios.repositorio.RepositorioException;


@WebService
public interface IServicioUsuarios {
	Usuario getById(String id) throws RepositorioException, EntidadNoEncontrada;
	
}
