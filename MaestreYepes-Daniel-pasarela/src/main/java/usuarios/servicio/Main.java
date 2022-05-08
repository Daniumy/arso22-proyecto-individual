package usuarios.servicio;

import java.time.LocalDateTime;
import java.util.Collections;

import usuarios.model.Usuario;
import usuarios.repositorio.EntidadNoEncontrada;
import usuarios.repositorio.RepositorioException;

public class Main {

	public static void main(String[] args) throws RepositorioException, EntidadNoEncontrada, IllegalArgumentException {
		ServicioUsuarios servicio = ServicioUsuarios.getInstancia();

		Usuario usuario = new Usuario();
		
		usuario.setRol("usuario");
		usuario.setUserId("Daniumy");

	}

}
