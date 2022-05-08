package usuarios.servicio;

import java.time.LocalDateTime;

import javax.jws.WebService;

import usuarios.model.Usuario;
import usuarios.repositorio.EntidadNoEncontrada;
import usuarios.repositorio.FactoriaRepositorioUsuarios;
import usuarios.repositorio.RepositorioException;
import usuarios.repositorio.RepositorioUsuarios;

@WebService(endpointInterface = "usuarios.servicio.IServicioUsuarios")
public class ServicioUsuarios implements IServicioUsuarios {

	private RepositorioUsuarios repositorio = FactoriaRepositorioUsuarios.getRepositorio();

	/** Patrón Singleton **/

	private static ServicioUsuarios instancia;

	private ServicioUsuarios() {

	}

	public static ServicioUsuarios getInstancia() {

		if (instancia == null)
			instancia = new ServicioUsuarios();

		return instancia;
	}

	@Override
	public Usuario getById(String id) throws RepositorioException, EntidadNoEncontrada {
		return repositorio.getById(id);
	}
}
