package com.pw.localizer.restful.resource.TEST;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.repository.location.LocationNetworkRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Patryk on 2016-09-24.
 */

@Path("test")
public class TestResource {
    @Inject
    private LocationNetworkRepository locationNetworkRepository;

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Location location(@PathParam("id") long id){
        return locationNetworkRepository.findById(id);
    }

    @GET
    @Path("/test1")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(){
        Bean bean = new Bean();
        bean.setName("ala");
        bean.setName2("kasia");
        bean.setName3("zosia");
        return Response.ok(bean).build();
    }

}
