package servicio;

import datosgob.ControladorAparcamientos;
import es.um.ciudad.*;
import json.ManejadorJson;
import model.Aparcamiento;
import model.InfoCiudad;
import org.xml.sax.SAXException;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import utils.Ciudades;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws JAXBException, RepositorioException, EntidadNoEncontrada, IOException,
			ParserConfigurationException, SAXException {
		// generarCiudades();
		printCiudad();
	}

	public static void generarCiudades() throws IOException, JAXBException, RepositorioException {
		IServicioCiudad servicio = ServicioCiudad.getInstancia();

		/* Lorca */

		Ciudad ciudad = new Ciudad();

		ciudad.setId(0);
		ciudad.setNombre(Ciudades.LORCA.getKey());
		ciudad.setCodigoPostal(Ciudades.LORCA.getValue());

		// Construir el contexto JAXB
		JAXBContext contexto = JAXBContext.newInstance("es.um.ciudad");

		// Obtener el árbol de contenido de un documento XML
		Unmarshaller unmarshaller = contexto.createUnmarshaller();
		ciudad.setParkingMovilidadReducida(
				(TypeParkingsMR) unmarshaller.unmarshal(new File("xml/parking-movilidad-reducida_30800.xml")));
		ciudad.setSitiosDeInteres(
				(TypeSitiosInteres) unmarshaller.unmarshal(new File("xml/sitios-de-interes_30800.xml")));

		servicio.create(ciudad);

		/* Málaga */

		ciudad = new Ciudad();

		ciudad.setNombre(Ciudades.MALAGA.getKey());
		ciudad.setCodigoPostal(Ciudades.MALAGA.getValue());
		ciudad.setSitiosDeInteres(ManejadorJson.getSitiosInteres(Ciudades.MALAGA));
		ciudad.setParkingMovilidadReducida(ControladorAparcamientos.getAparcamientosMalaga());

		servicio.create(ciudad);

		/* Tenerife */

		ciudad = new Ciudad();

		ciudad.setNombre(Ciudades.TENERIFE.getKey());
		ciudad.setCodigoPostal(Ciudades.TENERIFE.getValue());
		ciudad.setSitiosDeInteres(ManejadorJson.getSitiosInteres(Ciudades.TENERIFE));
		ciudad.setParkingMovilidadReducida(ControladorAparcamientos.getAparcamientosTenerife());

		servicio.create(ciudad);
	}

	public static void printCiudad() throws JAXBException, RepositorioException, EntidadNoEncontrada {
		IServicioCiudad servicio = ServicioCiudad.getInstancia();
		JAXBContext contexto = JAXBContext.newInstance("es.um.ciudad");

		// Obtener el �rbol de contenido de un documento XML
		Unmarshaller unmarshaller = contexto.createUnmarshaller();
		Ciudad ciudad = (Ciudad) unmarshaller.unmarshal(new File("ciudades/1.xml"));
		// Esto lo hemos usado para crear el id.xml de la ciudad Lorca, es necesario
		// hacerlo cada vez?
		// servicio.create(ciudad);

		// Obtener las ciudades que pueden ser consultadas.
		List<InfoCiudad> ciudadesConsultables = servicio.getCiudadesConsultables();
		ciudadesConsultables.forEach(System.out::println);

		// Conocer los sitios de interés turístico de una ciudad.
		ArrayList<TypeSitioInteres> sitiosInteresCiudad = (ArrayList<TypeSitioInteres>) servicio
				.getSitiosInteresCiudad(ciudad.getId());
		// System.out.println("Los sitios de interés de la ciudad " + ciudad.getNombre()
		// + " son:");

//        for (TypeSitioInteres sitioInteres : sitiosInteresCiudad) {
//            System.out.println("-----------------------------");
//            System.out.println("Nombre: " + sitioInteres.getNombre());
//            System.out.println("Descripcion: " + sitioInteres.getDescripcion());
//
//            TypeDireccion direccion = sitioInteres.getDireccion();
//            System.out.println("Dirección : " + direccion.getLat() + ", " + direccion.getLng());
//        }

		// Dado un sitio de interés, obtener plazas de aparcamiento cercanas.
		TypeSitioInteres sitioInteres = sitiosInteresCiudad.get(3);
		System.out.println("el sitio de interes es: " + sitioInteres.getNombre());
		List<TypeParking> parkingsCercanos = servicio.getAparcamientosSitioInteres(ciudad.getId(),
				sitioInteres.getId());
		for (TypeParking parking : parkingsCercanos) {
			System.out.println("La dirección de este aparcamiento es: " + parking.getDireccion());
			System.out.println("La latitud es: " + parking.getLatitud());
			System.out.println("La longitud es: " + parking.getLongitud());
		}

		// Obtener la información de una plaza de aparcamiento.
//        TypeParking parking = parkingsCercanos.get(0);
//        Aparcamiento parkingObtenido = servicio.getAparcamiento(ciudad.getId(), parking.getLatitud(), parking.getLongitud());
//        System.out.println("La dirección del parking es: " + parkingObtenido.getDireccion());
//        System.out.println("La imagen del parking es: " + parkingObtenido.getFoto());
//        System.out.println("La coordenada de latitud del parking es: " + parkingObtenido.getLatitud());
//        System.out.println("La coordenada de longitud del parking es: " + parkingObtenido.getLongitud());
	}
}
