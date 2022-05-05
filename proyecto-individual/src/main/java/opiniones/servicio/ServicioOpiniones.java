package opiniones.servicio;

import java.time.LocalDateTime;

import javax.json.bind.Jsonb;
import javax.json.bind.spi.JsonbProvider;
import javax.jws.WebService;

import org.xml.sax.SAXParseException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import opiniones.eventos.EventoValoracionCreada;
import opiniones.model.Opinion;
import opiniones.model.Valoracion;
import opiniones.repositorio.EntidadNoEncontrada;
import opiniones.repositorio.FactoriaRepositorioOpiniones;
import opiniones.repositorio.RepositorioException;
import opiniones.repositorio.RepositorioOpiniones;

@WebService(endpointInterface = "opiniones.servicio.IServicioOpiniones")
public class ServicioOpiniones implements IServicioOpiniones {

	private RepositorioOpiniones repositorio = FactoriaRepositorioOpiniones.getRepositorio();

	/** Patrón Singleton **/

	private static ServicioOpiniones instancia;

	private ServicioOpiniones() {

	}

	public static ServicioOpiniones getInstancia() {

		if (instancia == null)
			instancia = new ServicioOpiniones();

		return instancia;
	}

	// Registrar una URL para ser valorada (crea una opinión).
	@Override
	public String create(Opinion opinion) throws RepositorioException {
		// Control de integridad de los datos

		// 1. Campos obligatorios
		if (opinion.getUrl() == null || opinion.getUrl().isEmpty())
			throw new IllegalArgumentException("URL: no debe ser nulo ni vacio");

		if (repositorio.isUrlRepetida(opinion.getUrl()))
			throw new IllegalArgumentException("URL: solo puede haber una instancia por URL");

		for (Valoracion valoracion : opinion.getValoraciones()) {
			if (valoracion.getEmail() == null || valoracion.getEmail().isEmpty())
				throw new IllegalArgumentException("valoracion, email: no debe ser nulo ni vacio");
			if (valoracion.getCalificacion() < 1 || valoracion.getCalificacion() > 5)
				throw new IllegalArgumentException("calificacion: debe de estar entre 1 y 5");
			if (valoracion.getFechaRegistro() == null)
				throw new IllegalArgumentException("fechaRegistro: no debe ser nula");
		}

		String id = repositorio.add(opinion);

		return id;
	}

//Añadir una valoración para una URL. Si un usuario registra una segunda valoración para una misma URL, ésta reemplazará a la primera.
	@Override
	public void anadirValoracion(String url, Valoracion valoracion) throws RepositorioException, EntidadNoEncontrada {
		Opinion opinion = getByUrl(url);
		if (valoracion == null)
			throw new IllegalArgumentException("valoracion: no debe ser una valoracion nula");

		if (valoracion.getEmail() == null || valoracion.getEmail().isEmpty())
			throw new IllegalArgumentException("valoracion, email: no debe ser nulo ni vacio");
		if (valoracion.getCalificacion() < 1 || valoracion.getCalificacion() > 5)
			throw new IllegalArgumentException("calificacion: debe de estar entre 1 y 5");

		valoracion.setFechaRegistro(LocalDateTime.now());
		String emailNuevaValoracion = valoracion.getEmail();
		for (Valoracion valoracionaux : opinion.getValoraciones()) {
			if (valoracionaux.getEmail().equals(emailNuevaValoracion)) {
				valoracionaux.setCalificacion(valoracion.getCalificacion());
				valoracionaux.setComentario(valoracion.getComentario());
				valoracionaux.setFechaRegistro(valoracion.getFechaRegistro());
				repositorio.update(opinion);
				System.out.println("modificado uno");
				return;
			}
		}
		opinion.getValoraciones().add(valoracion);
		repositorio.update(opinion);

		// Notificar evento reserva creada
		// 1. Crear el evento
		EventoValoracionCreada evento = new EventoValoracionCreada();
		evento.setCalificacionMedia(opinion.getCalificacionMedia());
		evento.setNumValoraciones(opinion.getNumValoraciones());
		evento.setUrl(opinion.getUrl());
		evento.setValoracion(valoracion);
		// 2. Notificarlo
		notificarEvento(evento);
	}

	protected void notificarEvento(EventoValoracionCreada evento) {
		System.out.println("voy a notificar el evento" + evento.getUrl());
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri("amqps://wswcdlhl:pIJYGbkfqu-TYpyC0tKcDwEt-xQVTH6K@squid.rmq.cloudamqp.com/wswcdlhl");

			Connection connection = factory.newConnection();

			Channel channel = connection.createChannel();

			/** Declaración del Exchange **/

			final String exchangeName = "arso-exchange";

			boolean durable = true;
			channel.exchangeDeclare(exchangeName, "direct", durable);

			/** Envío del mensaje **/

			Jsonb contexto = JsonbProvider.provider().create().build();

			String cadenaJSON = contexto.toJson(evento);

			String mensaje = cadenaJSON;

			String routingKey = "arso";
			channel.basicPublish(exchangeName, routingKey,
					new AMQP.BasicProperties.Builder().contentType("application/json").build(), mensaje.getBytes());
			System.out.println("la variable mensaje luce tal que: "+mensaje);
			channel.close();
			connection.close();
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	@Override
	public Opinion getByUrl(String url) throws RepositorioException, EntidadNoEncontrada {
		return repositorio.getByUrl(url);
	}

	@Override
	public Opinion getById(String id) throws RepositorioException, EntidadNoEncontrada {
		return repositorio.getById(id);
	}

	@Override
	public void remove(String id) throws RepositorioException, EntidadNoEncontrada {
		Opinion opinion = repositorio.getById(id);

		repositorio.delete(opinion);

	}

	@Override
	public void removeAll() throws RepositorioException, EntidadNoEncontrada {
		for (Opinion opinion : repositorio.getAll()) {
			remove(opinion.getIdentificador());
		}

	}

	@Override
	public void removeUrl(String url) throws RepositorioException, EntidadNoEncontrada {
		Opinion opinion = getByUrl(url);

		repositorio.delete(opinion);
	}

}
