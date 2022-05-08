package repositorio;

import utils.PropertiesReader;

public class FactoriaRepositorioCiudades {

    private static final String PROPIEDAD_IMPLEMENTACION = "encuestas";

    private static RepositorioCiudades repository = null;

    public static RepositorioCiudades getRepositorio() {
        if (repository == null) {
            // Comprueba se existe una configuración específica para el repositorio
            try {
                PropertiesReader properties = new PropertiesReader(Repositorio.PROPERTIES);
                String clase = properties.getProperty(PROPIEDAD_IMPLEMENTACION);
                repository = (RepositorioCiudades) Class.forName(clase).getConstructor().newInstance();
            } catch (Exception e) {
                // Implementación por defecto
                repository = new RepositorioCiudadesXML();
            }

        }
        return repository;
    }


}

