package rest;

import model.InfoCiudad;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ListadoCiudades {

    public static class InfoExtendida {

        private String url;
        private InfoCiudad info;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public InfoCiudad getInfo() {
            return info;
        }

        public void setInfo(InfoCiudad info) {
            this.info = info;
        }

    }

    private List<InfoExtendida> ciudad;

    public List<InfoExtendida> getInfoCiudad() {
        return ciudad;
    }

    public void setInfoCiudad(List<InfoExtendida> ciudad) {
        this.ciudad = ciudad;
    }
}