//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.3.0 
// Visite <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2022.05.05 a las 01:45:22 AM CEST 
//


package es.um.ciudad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="codigoPostal" type="{http://www.um.es/ciudad}typeCodigoPostal"/&gt;
 *         &lt;element name="sitios-de-interes" type="{http://www.um.es/ciudad}typeSitiosInteres"/&gt;
 *         &lt;element name="parking-movilidad-reducida" type="{http://www.um.es/ciudad}typeParkingsMR"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "nombre",
    "codigoPostal",
    "sitiosDeInteres",
    "parkingMovilidadReducida"
})
@XmlRootElement(name = "ciudad")
public class Ciudad {

    protected int id;
    @XmlElement(required = true)
    protected String nombre;
    @XmlElement(required = true)
    protected String codigoPostal;
    @XmlElement(name = "sitios-de-interes", required = true)
    protected TypeSitiosInteres sitiosDeInteres;
    @XmlElement(name = "parking-movilidad-reducida", required = true)
    protected TypeParkingsMR parkingMovilidadReducida;

    /**
     * Obtiene el valor de la propiedad id.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoPostal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Define el valor de la propiedad codigoPostal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoPostal(String value) {
        this.codigoPostal = value;
    }

    /**
     * Obtiene el valor de la propiedad sitiosDeInteres.
     * 
     * @return
     *     possible object is
     *     {@link TypeSitiosInteres }
     *     
     */
    public TypeSitiosInteres getSitiosDeInteres() {
        return sitiosDeInteres;
    }

    /**
     * Define el valor de la propiedad sitiosDeInteres.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeSitiosInteres }
     *     
     */
    public void setSitiosDeInteres(TypeSitiosInteres value) {
        this.sitiosDeInteres = value;
    }

    /**
     * Obtiene el valor de la propiedad parkingMovilidadReducida.
     * 
     * @return
     *     possible object is
     *     {@link TypeParkingsMR }
     *     
     */
    public TypeParkingsMR getParkingMovilidadReducida() {
        return parkingMovilidadReducida;
    }

    /**
     * Define el valor de la propiedad parkingMovilidadReducida.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeParkingsMR }
     *     
     */
    public void setParkingMovilidadReducida(TypeParkingsMR value) {
        this.parkingMovilidadReducida = value;
    }

}
