package repositorio;

import es.um.ciudad.Ciudad;
import utils.Utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class RepositorioCiudadesXML implements RepositorioCiudades {

    public final static String DIRECTORIO_CIUDADES = "ciudades/";

    static {
        File directorio = new File(DIRECTORIO_CIUDADES);

        if (!directorio.exists())
            directorio.mkdir();
    }

    /*** Métodos de apoyo ***/

    public String getDocumento(int id) {
        return "ciudades/" + id + ".xml";
    }

    public boolean checkDocumento(int id) {
        final String documento = getDocumento(id);
        return new File(documento).exists();
    }

    protected void save(Ciudad ciudad) throws RepositorioException {
        final String documento = getDocumento(ciudad.getId());
        final File fichero = new File(documento);
        
        try {
            JAXBContext contexto = JAXBContext.newInstance("es.um.ciudad");
            Marshaller marshaller = contexto.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", true);
            marshaller.marshal(ciudad, fichero);
        } catch (Exception e) {
            throw new RepositorioException("Error al guardar la ciudad con id: " + ciudad.getId(), e);
        }
    }

    protected Ciudad load(int id) throws RepositorioException, EntidadNoEncontrada {

        if (!checkDocumento(id))
            throw new EntidadNoEncontrada("La ciudad no existe, id: " + id);

        final String documento = getDocumento(id);

        try {
            JAXBContext contexto = JAXBContext.newInstance("es.um.ciudad");
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            return (Ciudad) unmarshaller.unmarshal(new File(documento));
        } catch (Exception e) {
            throw new RepositorioException("Error al cargar la ciudad con id: " + id, e);
        }
    }

    /*** Fin métodos de apoyo ***/

    @Override
    public Integer add(Ciudad ciudad) throws RepositorioException {
        int id = Utils.getNewId();

        ciudad.setId(id);
        save(ciudad);

        return id;
    }

    @Override
    public void update(Ciudad ciudad) throws RepositorioException, EntidadNoEncontrada {
        if (!checkDocumento(ciudad.getId()))
            throw new EntidadNoEncontrada("La ciudad no existe, id: " + ciudad.getId());
        save(ciudad);
    }

    @Override
    public void delete(Ciudad ciudad) throws RepositorioException, EntidadNoEncontrada {
        if (!checkDocumento(ciudad.getId()))
            throw new EntidadNoEncontrada("La ciudad no existe, id: " + ciudad.getId());

        final String documento = getDocumento(ciudad.getId());

        File fichero = new File(documento);

        fichero.delete();
    }

    @Override
    public Ciudad getById(Integer id) throws RepositorioException, EntidadNoEncontrada {
        return load(id);
    }

    @Override
    public List<Ciudad> getAll() throws RepositorioException {
        LinkedList<Ciudad> resultado = new LinkedList<Ciudad>();

        for (int id : getIds()) {
            try {
                resultado.add(load(id));
            } catch (EntidadNoEncontrada e) {
                throw new RepositorioException("Error al cargar la ciudad: " + id, e);
            }
        }

        return resultado;
    }

    @Override
    public List<Integer> getIds() {
        File directorio = new File(DIRECTORIO_CIUDADES);
        File[] ciudades = directorio.listFiles(f -> f.isFile() && f.getName().endsWith(".xml"));

        LinkedList<Integer> resultado = new LinkedList<>();

        for (File file : ciudades) {
            String id = file.getName().substring(0, file.getName().length() - 4);
            resultado.add(Integer.parseInt(id));
        }

        return resultado;
    }
}
