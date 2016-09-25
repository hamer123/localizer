package com.pw.localizer.restful.resource.user;

import com.pw.localizer.model.dto.UserLastLocationsDTO;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.repository.user.UserRepository;
import com.pw.localizer.security.restful.Secured;

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

@Path("users")
//@Secured
public class UserRestController{
    @Inject
    private UserRepository userRepository;

    @GET
    @Path("{id}/last-locations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLastLocations(@PathParam("id")long id){
        User user = userRepository.findById(id);
        if(user == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        UserLastLocationsDTO userLastLocationsDTO = UserLastLocationsDTO.convertToDto(user);
        return Response.ok(userLastLocationsDTO).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id")long id){
        User user = userRepository.findById(id);
        if(user == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(user).build();
    }



}
