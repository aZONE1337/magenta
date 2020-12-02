package ru.magenta;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import ru.magenta.backend.CityAndDistance;
import ru.magenta.backend.DistanceCalculator;
import ru.magenta.entity.CityEntity;
import ru.magenta.entity.DistanceEntity;
import ru.magenta.exception.DifferentListSizeArguments;
import ru.magenta.service.CityDataAccess;
import ru.magenta.service.DistanceDataAccess;

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
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_XML)
    public Response getAllCities() {
        List<CityEntity> cities = CityDataAccess.getInstance().getAll();

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
                                      @QueryParam("from") final List<Integer> from,
                                      @QueryParam("to") final List<Integer> to) {
        if (from.size() != to.size()) {
            throw new DifferentListSizeArguments("Lists of cities have different sizes. Must be the same.");
        }

        CityDataAccess service = CityDataAccess.getInstance();

        List<CityEntity> citiesFrom = new ArrayList<>();
        List<CityEntity> citiesTo = new ArrayList<>();

        for (Integer id : from) {
            citiesFrom.add(service.get(id));
        }

        for (Integer id : to) {
            citiesTo.add(service.get(id));
        }

        List<String> crowFlightResp = new ArrayList<>();
        crowFlightResp.add("Crowflight calculation results:\n");

        List<String> matrixCalcResp = new ArrayList<>();
        matrixCalcResp.add("Distance matrix results:\n");

        if (type.equalsIgnoreCase("crowflight")) {
            for (int i = 0; i < citiesFrom.size(); i++) {
                crowFlightResp.add(
                        DistanceCalculator.calculateCrowFlight(
                                citiesFrom.get(i),
                                citiesTo.get(i)
                        ) + "\n");
            }
            return Response.ok()
                    .entity(crowFlightResp)
                    .build();
        }

        if (type.equalsIgnoreCase("datamatrix")) {
            for (int i = 0; i < citiesFrom.size(); i++) {
                matrixCalcResp.add(
                        DistanceCalculator.calculateDistanceMatrix(
                                citiesFrom.get(i),
                                citiesTo.get(i)
                        ) + "\n");
            }
            return Response.ok()
                    .entity(matrixCalcResp)
                    .build();
        }

        return Response
                .ok()
                .entity(crowFlightResp)
                .entity(matrixCalcResp)
                .build();
    }

    @POST
    @Path("/uploadFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response saveFromUpload(MultipartFormDataInput input) throws JAXBException {
        CityDataAccess cityDAO = CityDataAccess.getInstance();
        DistanceDataAccess distanceDAO = DistanceDataAccess.getInstance();

        StringBuilder file = new StringBuilder();

        JAXBContext context = JAXBContext.newInstance(CityAndDistance.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

//        String fileName = uploadForm.get("fileName").get(0).getBodyAsString();

        List<InputPart> inputParts = uploadForm.get("Attachment");

        for (InputPart inputPart : inputParts) {
            try {
                @SuppressWarnings("unused")
                MultivaluedMap<String, String> header = inputPart.getHeaders();

                InputStream in = inputPart.getBody(InputStream.class, null);

                file.append(Arrays.toString(IOUtils.toByteArray(in)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        CityAndDistance container = (CityAndDistance) unmarshaller.unmarshal(
                new StringReader(file.toString()));

        for (CityEntity city : container.getCities()) {
            cityDAO.save(city);
        }

        for (DistanceEntity distance : container.getDistances()) {
            distanceDAO.save(distance);
        }

        return Response.ok().build();
    }
}
