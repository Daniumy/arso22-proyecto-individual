package servicio;

import es.um.ciudad.Ciudad;
import org.xml.sax.SAXParseException;
import utils.Validador;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.util.List;

public class ValidadorCiudad {
	public static final String ESQUEMA = "xml/ciudad.xsd";

	public static List<SAXParseException> validar(Ciudad ciudad) {

		try {
			SchemaFactory factoriaSchema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			// Construye el esquema
			Schema esquema = factoriaSchema.newSchema(new File(ESQUEMA));

			// Solicita al esquema la construcción de un validador
			Validator validador = esquema.newValidator();

			// Registra el manejador de eventos de error
			Validador miValidador = new Validador();
			validador.setErrorHandler(miValidador);

			// Solicita la validación de los objetos JAXB
			JAXBContext contexto = JAXBContext.newInstance("es.um.ciudad");
			validador.validate(new JAXBSource(contexto, ciudad));
			  
			return miValidador.getErrores();
		} catch (Exception e) {

			return null; // No se ha realizado la validación 
		}

	}
}
