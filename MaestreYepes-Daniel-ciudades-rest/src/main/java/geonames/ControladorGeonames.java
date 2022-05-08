package geonames;

import utils.Format;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/* Servicios de GeoNames que pueden ser de utilidad
 * findNearbyWikipedia: sitios de interés de la ciudad
 * address: obtener la dirección a partir de la ltd/lng, almacenar este dato en el sitio de interés
 */

public class ControladorGeonames {
    private final static String API_URL = "http://api.geonames.org";
    private final static String API_AUTH = "&username=arso_martinezmaestre";

    public static InputStream findNearbyWikipedia(String postalCode, Format format) throws IOException {
        String service = "/findNearbyWikipedia";
        if (format == Format.JSON) {
            service += format;
        }

        String request = service + "?lang=es"
                + "&postalcode=" + postalCode
                + "&country=es&radius=20&maxRows=30";

        return new URL(API_URL + request + API_AUTH).openStream();
    }

    public static InputStream address(String lat, String lng, Format format) throws IOException {
        String service = "/address";
        if (format == Format.JSON) {
            service += format;
        }

        String request = service + "?lat=" + lat + "&lng=" + lng;
        return new URL(API_URL + request + API_AUTH).openStream();
    }
}