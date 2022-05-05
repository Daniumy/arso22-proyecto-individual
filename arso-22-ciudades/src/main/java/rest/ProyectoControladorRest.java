package rest;

import es.um.ciudad.Ciudad;
import es.um.ciudad.TypeParking;
import es.um.ciudad.TypeSitioInteres;
import io.swagger.annotations.*;
import model.Aparcamiento;
import model.InfoCiudad;
import model.SitioInteres;
import org.w3._2005.atom.Feed;
import org.w3._2005.atom.TypeAuthor;
import org.w3._2005.atom.TypeEntry;
import org.w3._2005.atom.TypeLink;
import servicio.ServicioCiudad;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Api
@Path("ciudades")
public class ProyectoControladorRest {

    private ServicioCiudad servicio = ServicioCiudad.getInstancia();

    @Context
    private UriInfo uriInfo;

    // curl -H "Accept: application/json" http://localhost:8080/api/ciudades

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Consulta las ciudades disponibles", notes = "Retorna las ciudades para las que hay información")
    public Response getCiudades()
            throws Exception {

        List<InfoCiudad> resultado = servicio.getCiudadesConsultables();

        LinkedList<ListadoCiudades.InfoExtendida> extendida = new LinkedList<>();

        for (InfoCiudad infoCiudad : resultado) {

            ListadoCiudades.InfoExtendida infoExtendida = new ListadoCiudades.InfoExtendida();

            infoExtendida.setInfo(infoCiudad);

            // URL

            int id = infoCiudad.getId();
            URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();

            infoExtendida.setUrl(nuevaURL.toString()); // string

            extendida.add(infoExtendida);

        }

        // Creamos un documento XML con un envoltorio

        ListadoCiudades listado = new ListadoCiudades();

        listado.setInfoCiudad(extendida);

        return Response.ok(listado).build();
    }

    // curl -H "Accept: application/xml" http://localhost:8080/api/ciudades/1

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Consulta una ciudad", notes = "Retorna una ciudad utilizando su id", response = Ciudad.class)
    @ApiResponses(value = {@ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Ciudad no encontrada")})
    public Response getCiudad(@ApiParam(value = "id de la ciudad", required = true) @PathParam("id") int id)
            throws Exception {

        return Response.status(Response.Status.OK).entity(servicio.getCiudad(id)).build();
    }

    // curl -H "Accept: application/xml" http://localhost:8080/api/ciudades/1/sitios-interes/Castillo_de_Lorca

    @GET
    @Path("/{ciudad_id}/sitios-interes/{sitio_interes_id}")
    @Encoded
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Consulta un sitio de interés", notes = "Retorna un sitio de interés utilizando su id", response = SitioInteres.class)
    @ApiResponses(value = {@ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Sitio de interés no encontrado")})
    public Response getSitioInteres(@ApiParam(value = "id de la ciudad", required = true) @PathParam("ciudad_id") int ciudadId,
                              @ApiParam(value = "id del sitio de interés", required = true) @PathParam("sitio_interes_id") String sitioInteresId)
            throws Exception {

        return Response.status(Response.Status.OK).entity(servicio.getSitioInteres(ciudadId, sitioInteresId)).build();
    }

    // curl -H "Accept: application/atom+xml" http://localhost:8080/api/ciudades/1/sitios-interes/atom

    @GET
    @Path("/{id}/sitios-interes/atom")
    @Produces({MediaType.APPLICATION_ATOM_XML})
    @ApiOperation(value = "Consulta los sitios de interés de una ciudad",
            notes = "Retorna una lista con la información de los sitios de interés de una ciudad")
    public Response getSitiosInteresCiudadAtom(@ApiParam(value = "id de la ciudad", required = true) @PathParam("id") int id)
            throws Exception {

        Ciudad ciudad = servicio.getCiudad(id);
        List<TypeSitioInteres> resultado = servicio.getSitiosInteresCiudad(id);

        Feed feed = new Feed();

        feed.setId(uriInfo.getAbsolutePath().toString());
        feed.setTitle("Sitios de interés de " + ciudad.getNombre());
        feed.setUpdated(Instant.now().toString());

        TypeAuthor author = new TypeAuthor();
        author.setName("arso22");

        feed.setAuthor(author);

        TypeLink link = new TypeLink();
        link.setRel("self");
        link.setHref(uriInfo.getAbsolutePath().toString());

        feed.setLink(link);

        List<TypeEntry> entries = feed.getEntry();

        for (TypeSitioInteres sitioInteres : resultado) {
            TypeEntry entry = new TypeEntry();

            URI resourceURI = uriInfo.getAbsolutePathBuilder().path(sitioInteres.getId()).build();

            entry.setId(resourceURI.toString());
            entry.setTitle(sitioInteres.getNombre());
            entry.setContent(sitioInteres.getDescripcion());
            entry.setUpdated(Instant.now().toString());

            entries.add(entry);
        }

        return Response.ok(feed).build();
    }

    // curl -H "Accept: application/json" http://localhost:8080/api/ciudades/1/sitios-interes

    @GET
    @Path("/{id}/sitios-interes")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Consulta los sitios de interés de una ciudad",
            notes = "Retorna una lista con la información de los sitios de interés de una ciudad")
    public Response getSitiosInteresCiudad(@ApiParam(value = "id de la ciudad", required = true) @PathParam("id") int id)
            throws Exception {

        return Response.ok(servicio.getSitiosInteresCiudad(id)).build();
    }

    // curl -H "Accept: application/json" http://localhost:8080/api/ciudades/1/aparcamientos/37.677381937417934,-1.7053470604319045

    @GET
    @Path("/{ciudad_id}/aparcamientos/{aparcamiento_id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Consulta el aparcamiento de una ciudad",
            notes = "Retorna un aparcamiento dado su id para una determinada ciudad",
            response = Aparcamiento.class)
    @ApiResponses(value = {@ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Aparcamiento no encontrado")})
    public Response getAparcamiento(
            @ApiParam(value = "id de la ciudad", required = true) @PathParam("ciudad_id") int ciudadId,
            @ApiParam(value = "id del aparcamiento", required = true) @PathParam("aparcamiento_id") String aparcamientoId)
            throws Exception {

        String[] coords = aparcamientoId.split(",");

        double lat = Double.parseDouble(coords[0]);
        double lng = Double.parseDouble(coords[1]);

        return Response.status(Response.Status.OK).entity(servicio.getAparcamiento(ciudadId, lat, lng)).build();
    }

    // curl -H "Accept: application/xml" http://localhost:8080/api/ciudades/1/sitios-interes/Castillo_de_Lorca/aparcamientos

    @GET
    @Path("/{ciudad_id}/sitios-interes/{sitio_interes_id}/aparcamientos")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Consulta los sitios de interés de una ciudad",
            notes = "Retorna una lista con la información de los sitios de interés de una ciudad")
    public Response getAparcamientosSitioInteres(@ApiParam(value = "id de la ciudad", required = true) @PathParam("ciudad_id") int ciudadId,
                                                 @ApiParam(value = "id del sitio de interés", required = true) @PathParam("sitio_interes_id") String sitioInteresId)
            throws Exception {

        List<TypeParking> resultado = servicio.getAparcamientosSitioInteres(ciudadId, sitioInteresId);

        LinkedList<ListadoAparcamientos.InfoExtendida> extendida = new LinkedList<>();

        for (TypeParking aparcamiento : resultado) {

            ListadoAparcamientos.InfoExtendida infoExtendida = new ListadoAparcamientos.InfoExtendida();

            infoExtendida.setInfo(aparcamiento);

            // URL

            String id = aparcamiento.getLatitud() + "," + aparcamiento.getLongitud();
            URI nuevaURL = new URI(uriInfo.getBaseUri() + "ciudades/" + ciudadId + "/aparcamientos/" + id);

            infoExtendida.setUrl(nuevaURL.toString()); // string

            extendida.add(infoExtendida);
        }

        // Creamos un documento XML con un envoltorio

        ListadoAparcamientos listado = new ListadoAparcamientos();

        listado.setAparcamientos(extendida);

        return Response.ok(listado).build();
    }

}
