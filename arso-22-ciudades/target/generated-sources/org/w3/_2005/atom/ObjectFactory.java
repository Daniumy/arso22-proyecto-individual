//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.3.0 
// Visite <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2022.05.05 a las 01:45:22 AM CEST 
//


package org.w3._2005.atom;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.w3._2005.atom package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.w3._2005.atom
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Feed }
     * 
     */
    public Feed createFeed() {
        return new Feed();
    }

    /**
     * Create an instance of {@link TypeAuthor }
     * 
     */
    public TypeAuthor createTypeAuthor() {
        return new TypeAuthor();
    }

    /**
     * Create an instance of {@link TypeLink }
     * 
     */
    public TypeLink createTypeLink() {
        return new TypeLink();
    }

    /**
     * Create an instance of {@link TypeEntry }
     * 
     */
    public TypeEntry createTypeEntry() {
        return new TypeEntry();
    }

}
