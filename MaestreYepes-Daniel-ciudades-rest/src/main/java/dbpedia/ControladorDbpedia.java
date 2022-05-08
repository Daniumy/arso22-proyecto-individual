package dbpedia;

import javax.json.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

public class ControladorDbpedia {

    private final static String API_URI = "https://dbpedia.org/data/";
    private final static String ES_API_URI = "https://es.dbpedia.org/data/";

    public static String getResourceName(String url) {
        String[] splitted = url.split("/");
        return splitted[splitted.length - 1];
    }

    public static DbpediaInfo getResourceData(String wikipediaUrl) throws IOException {
        String resource = getResourceName(wikipediaUrl);
        String request = ES_API_URI + resource + ".json";

        InputStreamReader fuente = new InputStreamReader(new URL(request).openStream());

        JsonReader jsonReader = Json.createReader(fuente);
        JsonObject obj = jsonReader.readObject();

        JsonObject resourceInfo = obj.getJsonObject("http://es.dbpedia.org/resource/" + resource);

        String description = "";
        if (resourceInfo != null) {
            JsonArray abstracts = resourceInfo.getJsonArray("http://dbpedia.org/ontology/abstract");

            if (abstracts != null) {
                description = abstracts.get(0).asJsonObject().getString("value");
            }
        }

        // Intentamos recuperar más información a partir de las entradas en inglés
        String photo = "";

        request = API_URI + resource + ".json";
        fuente = new InputStreamReader(new URL(request).openStream());

        jsonReader = Json.createReader(fuente);
        obj = jsonReader.readObject();

        if (!obj.isEmpty()) {
            resourceInfo = obj.getJsonObject("http://dbpedia.org/resource/" + resource);
            if (resourceInfo != null) {
                JsonArray redirects = resourceInfo.getJsonArray("http://dbpedia.org/ontology/wikiPageRedirects");
                JsonObject redirect = null;

                if (redirects != null) {
                    redirect = redirects.getJsonObject(0);
                } else {
                    redirect = obj.getJsonObject("http://dbpedia.org/resource/" + resource).getJsonArray("http://www.w3.org/2002/07/owl#sameAs").getJsonObject(0);
                }

                resource = getResourceName(redirect.getString("value"));
                request = API_URI + resource + ".json";
                fuente = new InputStreamReader(new URL(request).openStream());

                jsonReader = Json.createReader(fuente);
                obj = jsonReader.readObject();

                resourceInfo = obj.getJsonObject("http://dbpedia.org/resource/" + resource);
                // Descartamos entradas con resultados ambiguos
                if (resourceInfo != null
                        && resourceInfo.containsKey("http://dbpedia.org/ontology/thumbnail")) {
                    photo = obj.getJsonObject("http://dbpedia.org/resource/" + resource).
                            getJsonArray("http://dbpedia.org/ontology/thumbnail").
                            getJsonObject(0).
                            getString("value");
                }
            }
        }

        DbpediaInfo dbpediaInfo = new DbpediaInfo();

        if (!description.isEmpty()) {
            dbpediaInfo.setDescription(description);
        }

        if (!photo.isEmpty()) {
            System.out.println("photo ! empty");
            dbpediaInfo.setPhoto(photo);
        }

        return dbpediaInfo;
    }

}
