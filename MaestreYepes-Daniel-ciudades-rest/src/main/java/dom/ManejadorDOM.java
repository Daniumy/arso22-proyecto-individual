package dom;

import dbpedia.ControladorDbpedia;
import dbpedia.DbpediaInfo;
import geonames.ControladorGeonames;
import model.Direccion;
import model.SitioInteres;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utils.Format;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.LinkedList;
import java.util.List;

public class ManejadorDOM {

    public void generarSitiosInteres(String postalCode) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(ControladorGeonames.findNearbyWikipedia(postalCode, Format.XML));

        List<SitioInteres> sitiosInteres = new LinkedList<>();
        NodeList entries = doc.getElementsByTagName("entry");
        for (int i = 0; i < entries.getLength(); i++) {
            Element entry = (Element) entries.item(i);

            String nombre = entry.getElementsByTagName("title").item(0).getTextContent();
            String descripcion = entry.getElementsByTagName("summary").item(0).getTextContent();
            String lat = entry.getElementsByTagName("lat").item(0).getTextContent();
            String lng = entry.getElementsByTagName("lng").item(0).getTextContent();
            int rank = Integer.parseInt(entry.getElementsByTagName("rank").item(0).getTextContent());

            // Para cada sitio de interes -> address de GeoNames para obtener su dirección
            // a partir de las coordenadas
            Document addressDoc = db.parse(ControladorGeonames.address(lat, lng, Format.XML));

            String calle = "";
            Node node = addressDoc.getElementsByTagName("street").item(0);
            if (node != null) {
                calle = node.getTextContent();
            }

            String localidad = "";
            node = addressDoc.getElementsByTagName("locality").item(0);
            if (node != null) {
                localidad = node.getTextContent();
            }

            node = addressDoc.getElementsByTagName("postalcode").item(0);
            String codigoPostal = "";
            if (node != null) {
                codigoPostal = node.getTextContent();
            }

            int numero = 0;
            node = addressDoc.getElementsByTagName("houseNumber").item(0);
            if (node != null) {
                numero = Integer.parseInt(node.getTextContent());
            }

            Direccion direccion = new Direccion(calle, numero, localidad, codigoPostal, Double.parseDouble(lat), Double.parseDouble(lng));

            // Utilizamos la dbpedia para extender la información del sitio de interés
            String wikipediaUrl = entry.getElementsByTagName("wikipediaUrl").item(0).getTextContent();

            DbpediaInfo dbpediaInfo = ControladorDbpedia.getResourceData(wikipediaUrl);

            String dbpDescription = dbpediaInfo.getDescription();
            if (dbpDescription != null) {
                descripcion = dbpDescription;
            }

            String dbpPhoto = dbpediaInfo.getPhoto();

            SitioInteres sitioInteres = new SitioInteres(ControladorDbpedia.getResourceName(wikipediaUrl),
                    nombre, descripcion, dbpPhoto, direccion, rank);
            sitiosInteres.add(sitioInteres);
        }

        sitiosInteres.forEach(System.out::println);

        // Creamos un XML con los datos obtenidos
        Document outputDoc = db.newDocument();

        // 1. Creamos la raíz
        Element root = outputDoc.createElement("sitios-de-interes");

        // 2. Creamos un hijo por cada sitio de interés y lo enlazamos
        for (SitioInteres sitioInteres : sitiosInteres) {
            Element nodo = outputDoc.createElement("sitioInteres");

            Element id = outputDoc.createElement("id");
            Element nombre = outputDoc.createElement("nombre");
            Element descripcion = outputDoc.createElement("descripcion");
            Element foto = outputDoc.createElement("foto");
            Element rank = outputDoc.createElement("rank");

            Element direccion = outputDoc.createElement("direccion");
            Element calle = outputDoc.createElement("calle");
            Element numero = outputDoc.createElement("numero");
            Element localidad = outputDoc.createElement("localidad");
            Element codigoPostal = outputDoc.createElement("codigoPostal");
            Element lat = outputDoc.createElement("lat");
            Element lng = outputDoc.createElement("lng");

            id.appendChild(outputDoc.createTextNode(sitioInteres.getId()));
            nombre.appendChild(outputDoc.createTextNode(sitioInteres.getNombre()));
            descripcion.appendChild(outputDoc.createTextNode(sitioInteres.getDescripcion()));

            String photo = sitioInteres.getPhoto();
            if (photo == null) {
                foto = null;
            } else {
                foto.appendChild(outputDoc.createTextNode(photo));
            }

            rank.appendChild(outputDoc.createTextNode(Integer.toString(sitioInteres.getRank())));

            calle.appendChild(outputDoc.createTextNode(sitioInteres.getDireccion().getCalle()));
            numero.appendChild(outputDoc.createTextNode(Integer.toString(sitioInteres.getDireccion().getNumero())));
            localidad.appendChild(outputDoc.createTextNode(sitioInteres.getDireccion().getLocalidad()));
            codigoPostal.appendChild(outputDoc.createTextNode(sitioInteres.getDireccion().getCodigoPostal()));
            lat.appendChild(outputDoc.createTextNode(Double.toString(sitioInteres.getDireccion().getLat())));
            lng.appendChild(outputDoc.createTextNode(Double.toString(sitioInteres.getDireccion().getLng())));

            direccion.appendChild(calle);
            direccion.appendChild(numero);
            direccion.appendChild(localidad);
            direccion.appendChild(codigoPostal);
            direccion.appendChild(lat);
            direccion.appendChild(lng);

            nodo.appendChild(id);
            nodo.appendChild(nombre);
            nodo.appendChild(descripcion);

            if (foto != null) {
                nodo.appendChild(foto);
            }

            nodo.appendChild(rank);
            nodo.appendChild(direccion);
            root.appendChild(nodo);
        }

        outputDoc.appendChild(root);

        //  Guardamos el resultado en un fichero XML
        TransformerFactory tFactoria = TransformerFactory.newInstance();
        Transformer transformacion = tFactoria.newTransformer();

        Source input = new DOMSource(outputDoc);
        Result output = new StreamResult("xml/sitios-de-interes_" + postalCode + ".xml");
        transformacion.transform(input, output);
    }
}
