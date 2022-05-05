package datosgob;

import es.um.ciudad.TypeParking;
import es.um.ciudad.TypeParkingsMR;
import model.Aparcamiento;
import org.xml.sax.SAXException;
import sax.ManejadorSAX;
import utils.Ciudades;

import javax.json.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ControladorAparcamientos {

    private final static String PATH_MALAGA = "json/da_aparcamientosMovilidadReducida-4326.geojson";
    private final static String PATH_TENERIFE = "json/da_aparcamientosMovilidadReducidad-santa_cruz_de_tenerife.geojson";

    public static TypeParkingsMR getAparcamientosMalaga() throws IOException {
        InputStreamReader fuente = new InputStreamReader(new FileInputStream(PATH_MALAGA));

        JsonReader jsonReader = Json.createReader(fuente);
        JsonObject obj = jsonReader.readObject();

        JsonArray features = obj.getJsonArray("features");

        TypeParkingsMR aparcamientos = new TypeParkingsMR();
        features.forEach(f -> {
            JsonObject infoObj = f.asJsonObject();
            TypeParking aparcamiento = new TypeParking();

            JsonArray coords = infoObj
                    .getJsonObject("geometry")
                    .getJsonArray("coordinates");

            JsonString description = infoObj
                    .getJsonObject("properties")
                    .getJsonString("description");

            Pattern pattern = Pattern.compile("<b>Ubicación:</b>([^<]*)");
            Matcher matcher = pattern.matcher(description.getString());

            matcher.find();
            String direccion = matcher.group(1).trim();

            aparcamiento.setDireccion(direccion);
            aparcamiento.setLatitud(coords.getJsonNumber(1).doubleValue());
            aparcamiento.setLongitud(coords.getJsonNumber(0).doubleValue());

            aparcamientos.getParking().add(aparcamiento);
        });

        return aparcamientos;
    }

    public static TypeParkingsMR getAparcamientosTenerife() throws FileNotFoundException {
        InputStreamReader fuente = new InputStreamReader(new FileInputStream(PATH_TENERIFE));

        JsonReader jsonReader = Json.createReader(fuente);
        JsonObject obj = jsonReader.readObject();

        JsonArray features = obj.getJsonArray("features");

        TypeParkingsMR aparcamientos = new TypeParkingsMR();
        features.forEach(f -> {
            JsonObject infoObj = f.asJsonObject();
            TypeParking aparcamiento = new TypeParking();

            JsonArray coords = infoObj
                    .getJsonObject("geometry")
                    .getJsonArray("coordinates");

            JsonString direccion = infoObj
                    .getJsonObject("properties")
                    .getJsonString("DIRECCION");

            aparcamiento.setDireccion(direccion.getString());
            aparcamiento.setLatitud(coords.getJsonNumber(1).doubleValue());
            aparcamiento.setLongitud(coords.getJsonNumber(0).doubleValue());

            aparcamientos.getParking().add(aparcamiento);
        });

        return aparcamientos;
    }
}
