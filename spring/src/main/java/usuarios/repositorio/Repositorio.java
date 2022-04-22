package usuarios.repositorio;

import java.util.List;

/*
 *  Repositorio para entidades gestionadas con identificador.
 *  El parámetro T representa al tipo de datos de la entidad.
 *  El parámetro K es el tipo del identificador.
 */

public interface Repositorio<T, K> {

    String PROPERTIES = "repositorio.properties";
    
    T getById(K id) throws RepositorioException, EntidadNoEncontrada;
    
    List<T> getAll() throws RepositorioException;

    List<K> getIds();

}