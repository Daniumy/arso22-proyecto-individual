package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import opiniones.model.Opinion;
import opiniones.model.Valoracion;
import opiniones.repositorio.EntidadNoEncontrada;
import opiniones.repositorio.RepositorioException;
import opiniones.servicio.ServicioOpiniones;

public class testServicio {

	private ServicioOpiniones servicio;

	@Before
	public void setUp() throws Exception {
		servicio = ServicioOpiniones.getInstancia();
	}
	
	@After
	public void setUp2() throws Exception {
		servicio.removeAll();
	}

	@Test
	public void testCrearOpinionOk() throws RepositorioException {
		// Registrar una URL para ser valorada (crea una opinión).

		Opinion opinion = new Opinion();
		opinion.setUrl("https://www.urldeprueba.com");
		Valoracion valoracion1 = new Valoracion();
		valoracion1.setCalificacion(2.0);
		valoracion1.setEmail("pepe@gmail.com");
		valoracion1.setComentario("comentario ");
		valoracion1.setFechaRegistro(LocalDateTime.now());

		Collections.addAll(opinion.getValoraciones(), valoracion1);
		String id = servicio.create(opinion);
		assertTrue(true); // si llega hasta aquí significa que no ha saltado ningún exception.
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCrearOpinionErrorCampoUrl() throws RepositorioException {
		// Registrar una URL para ser valorada (crea una opinión).
		Opinion opinion = new Opinion();
		opinion.setUrl("");
		Valoracion valoracion1 = new Valoracion();
		valoracion1.setCalificacion(2.0);
		valoracion1.setEmail("pepe@gmail.com");
		valoracion1.setComentario("comentario ");
		valoracion1.setFechaRegistro(LocalDateTime.now());

		Collections.addAll(opinion.getValoraciones(), valoracion1);
		String id = servicio.create(opinion);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCrearOpinionErrorCampoValoracion() throws RepositorioException {
		// Registrar una URL para ser valorada (crea una opinión).
		Opinion opinion = new Opinion();
		opinion.setUrl("https://www.urldeprueba.com");
		Valoracion valoracion1 = new Valoracion();

		Collections.addAll(opinion.getValoraciones(), valoracion1);
		String id = servicio.create(opinion);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCrearOpinionErrorCampoEmail() throws RepositorioException {
		// Registrar una URL para ser valorada (crea una opinión).
		Opinion opinion = new Opinion();
		opinion.setUrl("https://www.urldeprueba.com");
		Valoracion valoracion1 = new Valoracion();
		valoracion1.setCalificacion(2.0);
		valoracion1.setEmail("");
		valoracion1.setComentario("comentario ");
		valoracion1.setFechaRegistro(LocalDateTime.now());

		Collections.addAll(opinion.getValoraciones(), valoracion1);
		String id = servicio.create(opinion);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCrearOpinionErrorCampoCalificacion1() throws RepositorioException {
		// Registrar una URL para ser valorada (crea una opinión).
		Opinion opinion = new Opinion();
		opinion.setUrl("https://www.urldeprueba.com");
		Valoracion valoracion1 = new Valoracion();
		valoracion1.setCalificacion(6.0);
		valoracion1.setEmail("pepe@gmail.com");
		valoracion1.setComentario("comentario ");
		valoracion1.setFechaRegistro(LocalDateTime.now());

		Collections.addAll(opinion.getValoraciones(), valoracion1);
		String id = servicio.create(opinion);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCrearOpinionErrorCampoCalificacion2() throws RepositorioException {
		// Registrar una URL para ser valorada (crea una opinión).
		Opinion opinion = new Opinion();
		opinion.setUrl("https://www.urldeprueba.com");
		Valoracion valoracion1 = new Valoracion();
		valoracion1.setCalificacion(0.5);
		valoracion1.setEmail("pepe@gmail.com");
		valoracion1.setComentario("comentario ");
		valoracion1.setFechaRegistro(LocalDateTime.now());

		Collections.addAll(opinion.getValoraciones(), valoracion1);
		String id = servicio.create(opinion);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCrearOpinionErrorCampoFechaRegistro() throws RepositorioException {
		// Registrar una URL para ser valorada (crea una opinión).
		Opinion opinion = new Opinion();
		opinion.setUrl("https://www.urldeprueba.com");
		Valoracion valoracion1 = new Valoracion();
		valoracion1.setCalificacion(2.0);
		valoracion1.setEmail("pepe@gmail.com");
		valoracion1.setComentario("comentario ");
		valoracion1.setFechaRegistro(null);

		Collections.addAll(opinion.getValoraciones(), valoracion1);
		String id = servicio.create(opinion);
	}

	@Test
	public void anadirValoracionOk() throws RepositorioException, EntidadNoEncontrada {
		// Registrar una URL para ser valorada (crea una opinión).
		Opinion opinion = new Opinion();
		opinion.setUrl("https://www.urldeprueba.com");
		Valoracion valoracion1 = new Valoracion();
		valoracion1.setCalificacion(2.0);
		valoracion1.setEmail("pepe@gmail.com");
		valoracion1.setComentario("comentario ");
		valoracion1.setFechaRegistro(LocalDateTime.now());
		Collections.addAll(opinion.getValoraciones(), valoracion1);
		String id = servicio.create(opinion);

		// Añadir una valoración para una URL. Si un usuario registra una segunda
		// valoración para una misma URL, ésta reemplazará a la primera.
		Valoracion valoracion3 = new Valoracion();
		valoracion3.setCalificacion(3.0);
		valoracion3.setEmail("alberto@gmail.com");
		valoracion3.setComentario("comentario de alberto");
		valoracion3.setFechaRegistro(LocalDateTime.now());
		servicio.anadirValoracion("https://www.urldeprueba.com", valoracion3);
		Opinion opinionRecuperada = servicio.getByUrl("https://www.urldeprueba.com");

		assertTrue(opinionRecuperada.getValoraciones().getLast().getEmail().equals(valoracion3.getEmail()));
	}

	@Test
	public void testAnadirModificarValoracionOk() throws RepositorioException, EntidadNoEncontrada {
		// Registrar una URL para ser valorada (crea una opinión).
		Opinion opinion = new Opinion();
		opinion.setUrl("https://www.urldeprueba.com");
		Valoracion valoracion1 = new Valoracion();
		valoracion1.setCalificacion(2.0);
		valoracion1.setEmail("pepe@gmail.com");
		valoracion1.setComentario("comentario ");
		valoracion1.setFechaRegistro(LocalDateTime.now());
		Collections.addAll(opinion.getValoraciones(), valoracion1);
		String id = servicio.create(opinion);

		// Añadir una valoración para una URL. Si un usuario registra una segunda
		// valoración para una misma URL, ésta reemplazará a la primera.
		Valoracion valoracion2 = new Valoracion();
		valoracion2.setCalificacion(3.0);
		valoracion2.setEmail("alberto@gmail.com");
		valoracion2.setComentario("comentario de alberto");
		valoracion2.setFechaRegistro(LocalDateTime.now());
		servicio.anadirValoracion("https://www.urldeprueba.com", valoracion2);

		Valoracion valoracion3 = new Valoracion();
		valoracion3.setCalificacion(4.0);
		valoracion3.setEmail("pepe@gmail.com");
		valoracion3.setComentario("comentario2");
		valoracion3.setFechaRegistro(LocalDateTime.now());
		servicio.anadirValoracion("https://www.urldeprueba.com", valoracion3);

		Opinion opinionRecuperada = servicio.getByUrl("https://www.urldeprueba.com");
		Valoracion primeraValoracion = opinionRecuperada.getValoraciones().getFirst();
		assertTrue(primeraValoracion.getEmail().equals(valoracion3.getEmail())
				&& primeraValoracion.getComentario().equals(valoracion3.getComentario())
				&& primeraValoracion.getCalificacion() == valoracion3.getCalificacion());
	}

	@Test(expected = EntidadNoEncontrada.class)
	public void testEliminarByUrl() throws RepositorioException, EntidadNoEncontrada {
		Opinion opinion = new Opinion();
		opinion.setUrl("https://www.urldeprueba.com");
		Valoracion valoracion1 = new Valoracion();
		valoracion1.setCalificacion(2.0);
		valoracion1.setEmail("pepe@gmail.com");
		valoracion1.setComentario("comentario ");
		valoracion1.setFechaRegistro(LocalDateTime.now());
		Collections.addAll(opinion.getValoraciones(), valoracion1);
		String id = servicio.create(opinion);
		//comprobamos que existe la url que acabamos de crear
		servicio.getByUrl("https://www.urldeprueba.com");
		assertTrue(servicio.getByUrl("https://www.urldeprueba.com") != null);
		// Eliminar url del servicio
		servicio.removeUrl("https://www.urldeprueba.com");
		//Comprobamos que se ha quitado correctamente intentando recuperarla de nuevo:
		servicio.getByUrl("https://www.urldeprueba.com");
		//se debe de devolver el exception: EntidadNoEncontrada ya que no existe, por lo tanto se ha eliminado correctamente
	}
	
	@Test(expected = EntidadNoEncontrada.class)
	public void testErrorEliminarByUrlInexistente() throws RepositorioException, EntidadNoEncontrada {
		Opinion opinion = new Opinion();
		opinion.setUrl("https://www.urldeprueba.com");
		Valoracion valoracion1 = new Valoracion();
		valoracion1.setCalificacion(2.0);
		valoracion1.setEmail("pepe@gmail.com");
		valoracion1.setComentario("comentario ");
		valoracion1.setFechaRegistro(LocalDateTime.now());
		Collections.addAll(opinion.getValoraciones(), valoracion1);
		String id = servicio.create(opinion);

		// Eliminar url del servicio
		servicio.removeUrl("https://www.urldepruebaINCORRECTA.com");
		//deberá de dar una exception EntidadNoEncontrada ya que no existe dicha URL
	}
}
