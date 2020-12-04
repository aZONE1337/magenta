package ru.magenta;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.magenta.exception.EmptyCityPassed;
import ru.magenta.exception.EmptyDistanceListPassed;
import ru.magenta.exception.UnreachablePoint;
import ru.magenta.util.CityAndDistance;
import ru.magenta.util.DistanceCalculator;
import ru.magenta.entity.CityEntity;
import ru.magenta.entity.DistanceEntity;
import ru.magenta.service.CityDataAccess;
import ru.magenta.service.DistanceDataAccess;
import ru.magenta.util.GraphLoader;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Path("/calculator")
public class CalculatorResource {
    GraphLoader graphLoader = GraphLoader.getInstance(true);

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_XML)
    public Response getAllCities() {
        List<CityEntity> cities = CityDataAccess.getInstance().getAll();

        //preparing XML-type response
        StringBuilder response = new StringBuilder("<cities>");
        for (CityEntity city : cities) {
            response.append(city.toString());
        }
        response.append("</cities>");

        return Response.ok().entity(response.toString()).build();
    }

    @GET
    @Path("/calculate")
    @Produces(MediaType.TEXT_PLAIN)
    public Response calculateDistance(@QueryParam("type") String type,
                                      @QueryParam("reload") boolean reload,
                                      @QueryParam("from") final List<Integer> from,
                                      @QueryParam("to") final List<Integer> to) {

        //determines the shortest list to trim by it
        int shortest = Math.min(from.size(), to.size());

        //reloads data from db to graph if needed
        if (reload)
            graphLoader = GraphLoader.getInstance(true);

        Graph<Integer, DefaultWeightedEdge> graph = graphLoader.getGraph();


        //preparing lists with objects to process calculations
        CityDataAccess service = CityDataAccess.getInstance();

        List<CityEntity> citiesFrom = new ArrayList<>();
        List<CityEntity> citiesTo = new ArrayList<>();

        for (Integer id : from) {
            citiesFrom.add(service.get(id));
        }

        for (Integer id : to) {
            citiesTo.add(service.get(id));
        }

        //preparing responses for both types
        List<String> crowFlightResp = new ArrayList<>();
        crowFlightResp.add("Crowflight calculation results:\n");

        List<String> matrixCalcResp = new ArrayList<>();
        matrixCalcResp.add("Distance matrix results:\n");

        if (type.equalsIgnoreCase("straight") || type.equalsIgnoreCase("all")) {
            for (int i = 0; i < shortest; i++) {
                try {
                    crowFlightResp.add("from(id): " + citiesFrom.get(i).getId()
                            + ", to(id): " + citiesTo.get(i).getId()
                            + ", distance: " + DistanceCalculator.calculateStraight(
                            citiesFrom.get(i),
                            citiesTo.get(i)
                    ) + "\n");
                } catch (EmptyCityPassed e) {
                    return Response.status(400).build();
                }
            }
        }

        if (type.equalsIgnoreCase("matrix") || type.equalsIgnoreCase("all")) {
            for (int i = 0; i < shortest; i++) {
                try {
                    matrixCalcResp.add("from(id): " + citiesFrom.get(i).getId()
                            + ", to(id): " + citiesTo.get(i).getId()
                            + ", distance: " + DistanceCalculator.calculateByMatrix(
                            citiesFrom.get(i),
                            citiesTo.get(i),
                            graph
                    ) + "\n");
                } catch (EmptyCityPassed | UnreachablePoint e) {
                    return Response.status(400).build();
                }
            }
        }

        List<String> response = new ArrayList<>();
        response.addAll(crowFlightResp);
        response.addAll(matrixCalcResp);

        return Response
                .ok()
                .entity(response)
                .build();
    }

    @POST
    @Path("/uploadFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response saveFromUpload(MultipartFormDataInput input) throws JAXBException {
        CityDataAccess cityDAO = CityDataAccess.getInstance();
        DistanceDataAccess distanceDAO = DistanceDataAccess.getInstance();

        //preparing unmarshaller
        JAXBContext context = JAXBContext.newInstance(CityAndDistance.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        //preparing inputstream
        InputPart inputPart = input.getFormDataMap().get("").get(0);
        InputStream uploadedInputStream;
        try {
            uploadedInputStream = inputPart.getBody(InputStream.class, null);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }

        //unmarshal uploaded file
        CityAndDistance container = (CityAndDistance) unmarshaller.unmarshal(uploadedInputStream);

        for (CityEntity city : container.getCities()) {
            cityDAO.save(city);
        }

        for (DistanceEntity distance : container.getDistances()) {
            distanceDAO.save(distance);
        }

        //updates graph
        graphLoader = GraphLoader.getInstance(true);

        return Response.ok().build();
    }
}
