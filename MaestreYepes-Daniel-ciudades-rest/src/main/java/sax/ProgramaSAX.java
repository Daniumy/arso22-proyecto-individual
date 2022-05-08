package sax;

import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import model.Aparcamiento;

public class ProgramaSAX {
    public static void main(String[] args) throws Exception {
        // 1. Obtener una factoría
        SAXParserFactory factoria = SAXParserFactory.newInstance();

        // 2. Pedir a la factoría la construcción del analizador
        SAXParser analizador = factoria.newSAXParser();

        // 3. Analizar el documento
        ManejadorSAX manejadorSAX = new ManejadorSAX();

        analizador.parse("xml/parking-movilidad-reducida_30800.xml", manejadorSAX);

        LinkedList<Aparcamiento> aparcamientos = manejadorSAX.getAparcamientos();
        for (Aparcamiento aparcamiento: aparcamientos) {
        	System.out.println(aparcamiento.toString());
        }
    }
}
