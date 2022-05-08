package rest;

import es.um.ciudad.TypeParking;
import es.um.ciudad.TypeSitioInteres;
import model.InfoCiudad;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ListadoAparcamientos {

    public static class InfoExtendida {

        private String url;
        private TypeParking info;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public TypeParking getInfo() {
            return info;
        }

        public void setInfo(TypeParking info) {
            this.info = info;
        }

    }

    private List<InfoExtendida> aparcamientos;

    public List<InfoExtendida> getAparcamientos() {
        return aparcamientos;
    }

    public void setAparcamientos(List<InfoExtendida> aparcamientos) {
        this.aparcamientos = aparcamientos;
    }
}