package servicio;

import java.util.List;

import es.um.ciudad.Ciudad;
import es.um.ciudad.TypeParking;
import es.um.ciudad.TypeSitioInteres;
import model.Aparcamiento;
import model.InfoCiudad;
import model.SitioInteres;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioCiudad {

    /**
     * Metodo de alta de una ciudad.
     *
     * @param ciudad debe ser valida respecto al modelo de dominio
     * @return identificador de la actividad
     */
    Integer create(Ciudad ciudad) throws RepositorioException;

    /**
     * Actualiza una actividad.
     *
     * @param ciudad debe ser valida respecto al modelo de dominio
     */
    void update(Ciudad ciudad) throws RepositorioException, EntidadNoEncontrada;

    /**
     * Recupera una ciudad utilizando el identificador.
     */
    Ciudad getCiudad(int id) throws RepositorioException, EntidadNoEncontrada;

    /**
     * Elimina una ciudad utilizando el identificador.
     */
    void removeCiudad(int id) throws RepositorioException, EntidadNoEncontrada;

    List<InfoCiudad> getCiudadesConsultables() throws RepositorioException;

    SitioInteres getSitioInteres(int ciudadId, String sitioInteresId) throws RepositorioException, EntidadNoEncontrada;

    List<TypeSitioInteres> getSitiosInteresCiudad(int id) throws RepositorioException, EntidadNoEncontrada;

    List<TypeParking> getAparcamientosSitioInteres(int ciudadId, String sitioInteresId) throws RepositorioException, EntidadNoEncontrada;

    Aparcamiento getAparcamiento(int ciudadId, double lat, double lng) throws EntidadNoEncontrada, RepositorioException;

}
