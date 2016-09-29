package com.pw.localizer.restful.resource.user;

import com.pw.localizer.model.dto.UserLastLocationsDTO;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.qualifier.PATCH;
import com.pw.localizer.repository.user.UserRepository;
import com.pw.localizer.security.restful.Secured;
import com.pw.localizer.service.user.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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
    @Inject
    private UserService userService;

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
        return Response.ok(UserLastLocationsDTO.convertToDto(user)).build();
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePartialUser(@Valid User user){
        user = userService.updatePartial(user);
        return Response.ok(UserLastLocationsDTO.convertToDto(user)).build();
    }

    @GET
    @Path("login/search/findByLoginLike")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogin(@NotNull @QueryParam("loginLike")String loginLike){
        return Response.ok(userRepository.findLoginByLoginLike(loginLike)).build();
    }
}
