package opiniones.rest;

import java.io.IOException;
import java.time.LocalDateTime;

import org.bson.types.ObjectId;

import opiniones.model.Opinion;
import opiniones.model.Valoracion;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.jaxb.JaxbConverterFactory;

public class ProgramaRetrofit {

	public static void main(String[] args) throws IOException {

		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/api/")
				.addConverterFactory(JacksonConverterFactory.create()).build();

		OpinionesRestClient service = retrofit.create(OpinionesRestClient.class);
		Opinion opinion = new Opinion();
		opinion.setUrl("https://www.urldeprueba.com");
		opinion.setId(new ObjectId());

		Valoracion valoracion1 = new Valoracion();
		valoracion1.setCalificacion(2.0);
		valoracion1.setEmail("pepe@gmail.com");
		valoracion1.setComentario("comentario");
		valoracion1.setFechaRegistro(LocalDateTime.now());

		opinion.getValoraciones().add(valoracion1);

		// Creación de una opinión
		Response<Void> respuesta = service.createOpinion(opinion).execute();
		System.out.println(respuesta);
		String url1 = respuesta.headers().get("Location");
		System.out.println(
				"(Si ya había sido creada(ejecutado) antes, dará un error 400 bad request). Encuesta creada: " + url1 + "\n");

		// Recupera la opinion con url "https://www.urldeprueba.com"
		Response<Opinion> respuesta1 = service.getOpinion("https://www.urldeprueba.com").execute();
		System.out.println("Respuesta de la opinion recuperada correctamente: " + respuesta1 + "\n");

		Response<Opinion> respuesta2 = service.getOpinion("https://www.urldepruebaINCORRECTA.com").execute();
		System.out.println("Respuesta de la opinion recuperada erróneamente: " + respuesta2 + "\n");

		//añadir valoración 
		Valoracion valoracion2 = new Valoracion();
		valoracion2.setCalificacion(3.0);
		valoracion2.setEmail("pepe@gmail.com");
		valoracion2.setComentario("comentario cambiado");
		valoracion2.setFechaRegistro(LocalDateTime.now());

		Response<Void> respuesta3 = service.anadirValoracion("https://www.urldeprueba.com", valoracion2).execute();
		System.out.println("Respuesta del intento correcto de modificacion de valoracion: " + respuesta3 + "\n");
		
		// Eliminación de opinión
		Response<Void> respuesta4 = service.removeOpinion("https://www.urldeprueba.com").execute();
		System.out.println("Respuesta del intento correcto de eliminacion de opinion: " + respuesta4 + "\n");

		Response<Void> respuesta5 = service.removeOpinion("https://www.urldeprueba.com").execute();
		System.out.println("Respuesta del intento erróneo de eliminacion de opinion: " + respuesta5 + "\n");
		
	}

}
