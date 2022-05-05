package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import es.um.ciudad.Ciudad;
import es.um.ciudad.TypeParking;
import es.um.ciudad.TypeParkingsMR;
import es.um.ciudad.TypeSitiosInteres;
import es.um.ciudad.TypeSitioInteres;
import servicio.ServicioCiudad;
import utils.Ciudades;
import utils.Utils;

public class ProgramaJAXB {

    public static void main(String[] args) throws Exception {

        // Construir el contexto JAXB
        JAXBContext contexto = JAXBContext.newInstance("es.um.ciudad");

        // Obtener el árbol de contenido de un documento XML
        Unmarshaller unmarshaller = contexto.createUnmarshaller();
        TypeSitiosInteres sitiosInteresContainer = (TypeSitiosInteres) unmarshaller.unmarshal(new File("xml/sitios-de-interes_30800.xml"));
        TypeParkingsMR parkingMovilidadReducidaContainer = (TypeParkingsMR) unmarshaller.unmarshal(new File("xml/parking-movilidad-reducida_30800.xml"));

        Ciudad ciudad = new Ciudad();
        ciudad.setId(0);
        ciudad.setNombre(Ciudades.LORCA.getKey());
        ciudad.setCodigoPostal(Ciudades.LORCA.getValue());

        ciudad.setParkingMovilidadReducida(parkingMovilidadReducidaContainer);
        ciudad.setSitiosDeInteres(sitiosInteresContainer);

        // Marshaller marshaller = contexto.createMarshaller();
        // marshaller.setProperty("jaxb.formatted.output", true);
        // marshaller.setProperty("jaxb.schemaLocation", "http://www.um.es/ciudad ciudad.xsd");
        // marshaller.marshal(ciudad, new File("xml/ciudad.xml"));

        ServicioCiudad servicioCiudad = ServicioCiudad.getInstancia();
        servicioCiudad.create(ciudad);

    }

}
