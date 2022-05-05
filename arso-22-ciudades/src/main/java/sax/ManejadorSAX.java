package sax;

import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Aparcamiento;
import utils.Ciudades;

public class ManejadorSAX extends DefaultHandler {
    private boolean dentroDireccion = false;
    private boolean dentroLongitud = false;
    private boolean dentroLatitud = false;
    private Aparcamiento aparcamientoActual = null;
    private StringBuilder data = null;

    private LinkedList<Aparcamiento> aparcamientos = new LinkedList<>();

    public LinkedList<Aparcamiento> getAparcamientos() {
        return new LinkedList<>(aparcamientos);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "parking":
                aparcamientoActual = new Aparcamiento(Ciudades.LORCA.getKey(), null, 0d, 0d, 0, 0, null);
                break;
            case "direccion":
                dentroDireccion = true;
                break;
            case "longitud":
                dentroLongitud = true;
                break;
            case "latitud":
                dentroLatitud = true;
                break;
            default:
                break;
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (dentroDireccion) {
            aparcamientoActual.setDireccion(data.toString());
            dentroDireccion = false;
        } else if (dentroLongitud) {
            aparcamientoActual.setLongitud(Double.parseDouble(data.toString()));
            dentroLongitud = false;
        } else if (dentroLatitud) {
            aparcamientoActual.setLatitud(Double.parseDouble(data.toString()));
            dentroLatitud = false;
        }

        if (qName.equals("parking")) {
            aparcamientos.add(aparcamientoActual);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String contenido = new String(ch, start, length);
        data.append(contenido);
    }
}
