package servicio;

import es.um.ciudad.*;
import eventos.EventoValoracionCreada;
import model.Aparcamiento;
import model.Direccion;
import model.InfoCiudad;
import model.SitioInteres;
import org.xml.sax.SAXParseException;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorioCiudades;
import repositorio.RepositorioCiudades;
import repositorio.RepositorioException;
import utils.Utils;

import javax.json.bind.Jsonb;
import javax.json.bind.spi.JsonbProvider;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServicioCiudad implements IServicioCiudad {

	private RepositorioCiudades repositorio = FactoriaRepositorioCiudades.getRepositorio();

	private static ServicioCiudad instancia;

	public static ServicioCiudad getInstancia() {
		if (instancia == null)
			instancia = new ServicioCiudad();

		return instancia;
	}

	private EventoValoracionCreada evento = null;

	private ServicioCiudad() {
		// TODO Relleno

		// registro consumidor eventos

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri("amqps://wswcdlhl:pIJYGbkfqu-TYpyC0tKcDwEt-xQVTH6K@squid.rmq.cloudamqp.com/wswcdlhl");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			/** Declaración de la cola y enlace con el exchange **/

			final String exchangeName = "arso-exchange";
			final String queueName = "arso-queue-parkings";
			final String bindingKey = "valoracionParking";

			boolean durable = true;
			boolean exclusive = false;
			boolean autodelete = false;
			Map<String, Object> properties = null; // sin propiedades
			channel.queueDeclare(queueName, durable, exclusive, autodelete, properties);
			channel.queueBind(queueName, exchangeName, bindingKey);

			/** Configuración del consumidor **/
			boolean autoAck = false;
			String cola = "arso-queue-parkings";
			String etiquetaConsumidor = "arso-consumidor";
			// Consumidor push

			channel.basicConsume(cola, autoAck, etiquetaConsumidor,

					new DefaultConsumer(channel) {
						@Override
						public void handleDelivery(String consumerTag, Envelope envelope,
								AMQP.BasicProperties properties, byte[] body) throws IOException {

							String routingKey = envelope.getRoutingKey();
							String contentType = properties.getContentType();
							long deliveryTag = envelope.getDeliveryTag();

							String contenido = new String(body);

							Jsonb contexto = JsonbProvider.provider().create().build();

							evento = contexto.fromJson(contenido, EventoValoracionCreada.class);
							// Procesamos el evento
							try {
								anadirInfoAparcamiento();
							} catch (RepositorioException | EntidadNoEncontrada e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							// Confirma el procesamiento
							channel.basicAck(deliveryTag, false);
						}
					});
		} catch (

		Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Métodos de apoyo
	 * 
	 * @throws RepositorioException
	 * @throws EntidadNoEncontrada
	 **/
	protected void anadirInfoAparcamiento() throws RepositorioException, EntidadNoEncontrada {
		List<Ciudad> ciudades = repositorio.getAll();
		String url = evento.getUrl();
		String partes[] = url.split("/");
		System.out.println("el evento recibido tiene la url: " + url);
		String latLng[] = partes[3].split(",");
		for (Ciudad ciudad : ciudades) {
			TypeParkingsMR parkingsWrapper = ciudad.getParkingMovilidadReducida();
			List<TypeParking> parkings = parkingsWrapper.getParking();
			for (TypeParking parking : parkings) {
				String lat = Double.toString(parking.getLatitud());
				String lng = Double.toString(parking.getLongitud());
				if (latLng[0].equals(lat) && latLng[1].equals(lng)) {
					parking.setCalificacionMedia(evento.getCalificacionMedia());
					parking.setUrl(url);
					parking.setNumValoraciones(evento.getNumValoraciones());
					update(ciudad);
					return;
				}
			}
		}

	}

	protected void validar(Ciudad ciudad) throws IllegalArgumentException {

		List<SAXParseException> resultadoValidacion = ValidadorCiudad.validar(ciudad);

		if (!resultadoValidacion.isEmpty())
			// FIXME simplificación: Se incluye en la información el mensaje de la primera
			// excepción

			throw new IllegalArgumentException(
					"La ciudad no cumple el esquema: " + resultadoValidacion.get(0).getMessage());

	}

	/**
	 * Fin de métodos de apoyo
	 **/

	@Override
	public Integer create(Ciudad ciudad) throws RepositorioException {
		// Según el esquema, el id es obligatorio
		// Se establece uno provisional, el repositorio aportará el definitivo
		ciudad.setId(0);

		validar(ciudad);

		return repositorio.add(ciudad);
	}

	@Override
	public void update(Ciudad ciudad) throws RepositorioException, EntidadNoEncontrada {
		validar(ciudad);
		repositorio.update(ciudad);
	}

	@Override
	public Ciudad getCiudad(int id) throws RepositorioException, EntidadNoEncontrada {
		return repositorio.getById(id);
	}

	@Override
	public void removeCiudad(int id) throws RepositorioException, EntidadNoEncontrada {
		Ciudad ciudad = repositorio.getById(id);
		repositorio.delete(ciudad);

	}

	public List<InfoCiudad> getCiudadesConsultables() throws RepositorioException {
		return repositorio.getAll().stream().map(c -> new InfoCiudad(c.getId(), c.getNombre(), c.getCodigoPostal()))
				.collect(Collectors.toList());
	}

	// Conocer los sitios de interés turístico de una ciudad.
	public List<TypeSitioInteres> getSitiosInteresCiudad(int id) throws RepositorioException, EntidadNoEncontrada {
		List<TypeSitioInteres> resultado = new ArrayList<>();
		Ciudad ciudad = getCiudad(id);
		TypeSitiosInteres sitiosInteresContainer = ciudad.getSitiosDeInteres();

		return sitiosInteresContainer.getSitioInteres();
	}

	public SitioInteres getSitioInteres(int ciudadId, String sitioInteresId)
			throws RepositorioException, EntidadNoEncontrada {
		Ciudad ciudad = repositorio.getById(ciudadId);
		List<TypeSitioInteres> sitiosInteres = ciudad.getSitiosDeInteres().getSitioInteres();

		TypeSitioInteres sitioInteres = sitiosInteres.stream().filter(s -> s.getId().equals(sitioInteresId)).findFirst()
				.orElse(null);

		if (sitioInteres != null) {
			TypeDireccion typeDireccion = sitioInteres.getDireccion();
			Direccion direccion = new Direccion(typeDireccion.getCalle(), typeDireccion.getNumero().intValue(),
					typeDireccion.getLocalidad(), typeDireccion.getCodigoPostal(), typeDireccion.getLat(),
					typeDireccion.getLng());

			return new SitioInteres(sitioInteres.getId(), sitioInteres.getNombre(), sitioInteres.getDescripcion(),
					sitioInteres.getFoto(), direccion, sitioInteres.getRank().intValue());
		}

		return null;
	}

	public Aparcamiento getAparcamiento(int ciudadId, double lat, double lng)
			throws EntidadNoEncontrada, RepositorioException {
		Ciudad ciudad = repositorio.getById(ciudadId);
		TypeParkingsMR aparcamientos = ciudad.getParkingMovilidadReducida();

		TypeParking parking = aparcamientos.getParking().stream()
				.filter(p -> p.getLatitud() == lat && p.getLongitud() == lng).findFirst().orElse(null);

		if (parking != null) {
			Aparcamiento aparcamiento = new Aparcamiento(ciudad.getNombre(), parking.getDireccion(),
					parking.getLongitud(), parking.getLatitud(), parking.getNumValoraciones(),
					parking.getCalificacionMedia(), parking.getUrl());

			aparcamiento.setFoto(parking.getFoto());

			return aparcamiento;
		}

		return null;
	}

	// Dado un sitio de interés, obtener plazas de aparcamiento cercanas.
	// (consideraremos cercanas a menos de 300 metros.)
	public List<TypeParking> getAparcamientosSitioInteres(int ciudadId, String sitioInteresId)
			throws RepositorioException, EntidadNoEncontrada {
		Ciudad ciudad = repositorio.getById(ciudadId);
		SitioInteres sitioInteres = getSitioInteres(ciudadId, sitioInteresId);
		Direccion direccion = sitioInteres.getDireccion();

		return ciudad.getParkingMovilidadReducida().getParking().stream().filter(p -> {
			double distanciaKm = Utils.distance(p.getLatitud(), p.getLongitud(), direccion.getLat(), direccion.getLng(),
					"K");

			return distanciaKm < 0.3;
		}).collect(Collectors.toList());
	}

}
