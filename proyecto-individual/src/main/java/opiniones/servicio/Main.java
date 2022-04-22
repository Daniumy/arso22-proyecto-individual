package opiniones.servicio;

import java.time.LocalDateTime;
import java.util.Collections;

import opiniones.model.Opinion;
import opiniones.model.Valoracion;
import opiniones.repositorio.EntidadNoEncontrada;
import opiniones.repositorio.RepositorioException;

public class Main {

	public static void main(String[] args) throws RepositorioException, EntidadNoEncontrada, IllegalArgumentException {
		ServicioOpiniones servicio = ServicioOpiniones.getInstancia();

		// Configura la opinion
		
		Opinion opinion = new Opinion();
		
		opinion.setUrl("www.urldeprueba.com");
		
		// curl -i -X POST -H "Content-Type: application/json" -d @testfiles/1.json http://localhost:8080/api/opiniones/
		// Registrar una URL para ser valorada (crea una opinión).
		String id = servicio.create(opinion);
		
		//Recuperar la opinión de una URL.
		//curl http://localhost:8080/api/opiniones/https://www.urldeprueba.com
//		servicio.getByUrl("https://www.urldeprueba.com");
//		
		//curl -i -X POST -H "Content-Type: application/json" -d @testfiles/valoracion.json http://localhost:8080/api/opiniones/https://www.urldeprueba.com
		//Añadir una valoración para una URL. Si un usuario registra una segunda valoración para una misma URL, ésta reemplazará a la primera.
//		Valoracion valoracion3 = new Valoracion();
//		valoracion3.setCalificacion(3.0);
//		valoracion3.setEmail("alberto@gmail.com");
//		valoracion3.setComentario("comentario amai3");
//		valoracion3.setFechaRegistro(LocalDateTime.now());
//		
//		servicio.anadirValoracion("https://www.urldeprueba.com", valoracion3 );
//		
		
		//curl -i -X DELETE http://localhost:8080/api/opiniones/https://www.urldeprueba.com
		//eliminar url del servicio
		//servicio.removeUrl("https://www.urldeprueba.com");
	}

}
