package opiniones.repositorio;

import opiniones.model.Opinion;

public interface RepositorioOpiniones extends Repositorio<Opinion, String> {

	Opinion getByUrl(String url) throws EntidadNoEncontrada;

	boolean isUrlRepetida(String url);
	
}
