package usuarios.repositorio;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/*
 * Excepción que representa una excepción del repositorio.
 * Al instanciarla, se establece la excepción interna que produce el error (causa).
 */

@SuppressWarnings("serial")
public class RepositorioException extends Exception {

    public RepositorioException(String msg, Throwable causa) {
        super(msg, causa);
    }

    public RepositorioException(String msg) {
        super(msg);
    }

}

