package json;

import dbpedia.ControladorDbpedia;
import dbpedia.DbpediaInfo;
import es.um.ciudad.TypeDireccion;
import es.um.ciudad.TypeSitioInteres;
import es.um.ciudad.TypeSitiosInteres;
import geonames.ControladorGeonames;
import utils.Ciudades;
import utils.Format;

import javax.json.*;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyNamingStrategy;
import javax.json.bind.config.PropertyOrderStrategy;
import javax.json.bind.spi.JsonbProvider;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class ManejadorJson {

    static JsonbConfig config = new JsonbConfig()
            .withNullValues(true)
            .withFormatting(true)
            .withPropertyNamingStrategy(
                    PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES)
            .withPropertyOrderStrategy(
                    PropertyOrderStrategy.LEXICOGRAPHICAL);

    public static TypeSitiosInteres getSitiosInteres(Ciudades c) throws IOException {
        InputStream fuente = ControladorGeonames.findNearbyWikipedia(c.getValue(), Format.JSON);

        JsonReader jsonReader = Json.createReader(fuente);
        JsonObject obj = jsonReader.readObject();

        JsonArray geonames = obj.getJsonArray("geonames");

        TypeSitiosInteres sitiosInteres = new TypeSitiosInteres();

        for (JsonValue o : geonames) {
            JsonObject infoObj = o.asJsonObject();
            TypeSitioInteres sitioInteres = new TypeSitioInteres();

            System.out.println(infoObj);

            sitioInteres.setNombre(infoObj.getString("title"));

            JsonValue summary = infoObj.get("summary");
            if (summary != null) {
                sitioInteres.setDescripcion(summary.toString());
            } else {
                sitioInteres.setDescripcion("");
            }

            sitioInteres.setRank(BigInteger.valueOf(infoObj.getInt("rank")));

            // Para cada sitio de interes -> address de GeoNames para obtener su dirección
            // a partir de las coordenadas
            sitioInteres.setDireccion(getDireccion(infoObj));


            // Utilizamos la dbpedia para extender la información del sitio de interés
            String wikipediaUrl = infoObj.getString("wikipediaUrl");

            DbpediaInfo dbpediaInfo = ControladorDbpedia.getResourceData(wikipediaUrl);

            String dbpDescription = dbpediaInfo.getDescription();
            if (dbpDescription != null) {
                sitioInteres.setDescripcion(dbpDescription);
            }

            sitioInteres.setFoto(dbpediaInfo.getPhoto());

            sitiosInteres.getSitioInteres().add(sitioInteres);
        }

        return sitiosInteres;
    }

    public static TypeDireccion getDireccion(JsonObject sitioInteres) throws IOException {
        Double lat = sitioInteres.getJsonNumber("lat").doubleValue();
        Double lng = sitioInteres.getJsonNumber("lng").doubleValue();

        JsonReader jsonReader = Json.createReader(ControladorGeonames
                .address(lat.toString(), lng.toString(), Format.JSON));
        JsonObject obj = jsonReader.readObject();

        JsonObject address = obj.getJsonObject("address");

        TypeDireccion direccion = new TypeDireccion();
        direccion.setLat(lat);
        direccion.setLng(lng);

        if (address == null) {
            direccion.setCalle("");
            direccion.setLocalidad("");
            direccion.setCodigoPostal("");
            direccion.setNumero(BigInteger.ZERO);
            return direccion;
        }

        direccion.setCalle(address.getString("street"));
        direccion.setLocalidad(address.getString("locality"));
        direccion.setCodigoPostal(address.getString("postalcode"));
        direccion.setNumero(BigInteger.valueOf(Integer.parseInt(address.getString("houseNumber"))));

        return direccion;
    }
}
