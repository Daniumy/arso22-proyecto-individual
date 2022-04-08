package opiniones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlTransient;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Opinion {

		@BsonId
		@JsonIgnore
		private ObjectId id;
		private String url;
		private LinkedList<Valoracion> valoraciones = new LinkedList<>();

		//Getters and setters
		@XmlTransient
		public ObjectId getId() {
			return id;
		}

		public void setId(ObjectId id) {
			this.id = id;
		}
		
		public String getIdentificador() {
			return this.id.toString();
		}
		
		public void setIdentificador(String identificador) {
			this.id = new ObjectId(identificador);
		}
		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public LinkedList<Valoracion> getValoraciones() {
			return valoraciones;
		}

		public void setValoraciones(LinkedList<Valoracion> valoraciones) {
			this.valoraciones = valoraciones;
		}

		//Propiedades calculadas
		
		public int getNumValoraciones() {
			return this.valoraciones.size();
		}
		
		public double getCalificacionMedia() {
			double response = 0;
			for (Valoracion valoracion : getValoraciones()) {
				response += valoracion.getCalificacion();
			}
			return response / getValoraciones().size();
		}

		@Override
		public String toString() {
			return "Opinion [id=" + id + ", url=" + url + ", valoraciones=" + valoraciones + ", getNumValoraciones()="
					+ getNumValoraciones() + ", getCalificacionMedia()=" + getCalificacionMedia() + "]";
		}
		
		
		
}
