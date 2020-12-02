package ru.magenta;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

@ApplicationPath("/api")
public class RestApplication extends Application {
    @GET
    @Path("/test")
    public Response doTest() {
        return Response.ok()
                .entity("I'm alive")
                .build();
    }
}
