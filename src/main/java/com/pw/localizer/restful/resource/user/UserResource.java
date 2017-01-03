package com.pw.localizer.restful.resource.user;

import com.pw.localizer.jackson.CustomFields;
import com.pw.localizer.jackson.CustomFieldsValidation;
import com.pw.localizer.model.dto.UserDTO;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.entity.UserSetting;
import com.pw.localizer.repository.user.UserRepository;
import com.pw.localizer.restful.provider.filter.LogRequest;
import com.pw.localizer.security.restful.Secured;
import com.pw.localizer.service.user.UserService;
import com.pw.localizer.service.utilitis.DiscoverLazyFetch;
import lombok.Getter;
import lombok.Setter;
import org.dozer.Mapper;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("users")
@LogRequest
@Secured
@Setter
@Getter
public class UserResource {
    @Inject
    private UserRepository userRepository;
    @Inject
    private UserService userService;
    @Inject
    private Mapper mapper;
    @Inject
    private DiscoverLazyFetch discoverLazyFetch;
    @Inject
    private CustomFieldsValidation customFieldsValidation;
    @Inject
    private Logger logger;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id")long id) {
        User user = userRepository.findById(id);
        if(user == null)
            throw new NoResultException("User has not been founded with id " + id);
        UserDTO userDTO = mapper.map(user, UserDTO.class, "full");
        return Response.ok(userDTO).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("size") @DefaultValue("100") @Max(250) @Min(1) Integer size,
                           @QueryParam("offset") @DefaultValue("0") @Min(0) Integer offset) {
        List<User>userList = userRepository.findAll(offset,size);
        List<UserDTO>userDTOList = new ArrayList<>();
        userList.forEach(u -> {
            u = discoverLazyFetch.discoverAndSetToNull(u);
            userDTOList.add(mapper.map(u, UserDTO.class, "full"));
        });
        return Response.ok(userDTOList).build();
    }

    @GET
    @Path("{id}/lastLocations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLastLocations(@PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if(user == null) {
            throw new NoResultException("User has not been founded with id " + id);
        }
        user = discoverLazyFetch.discoverAndSetToNull(user);
        UserDTO userDTO = mapper.map(user, UserDTO.class, "full");
        return Response.ok(userDTO.getLastLocations()).build();
    }

    @GET
    @Path("search/findLoginByLoginLike")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogin(@QueryParam("loginLike") @NotNull String loginLike){
        return Response.ok(userRepository.findLoginByLoginLike(loginLike)).build();
    }

    @GET
    @Path("search/findByIdIn")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomUser(@QueryParam("idList") Set<Long> idList,
                                  @QueryParam("fieldSet") Set<String> fieldSet) throws NoSuchFieldException {
        //validate idList
        if(idList.isEmpty()) {
            throw new BadRequestException("Parameter idList has to be provided, at least 1");
        }
        //validate filed names
        if(fieldSet != null) {
            for(String field : fieldSet) {
                if(!customFieldsValidation.validateField(UserDTO.class, field)) {
                    throw new NoSuchFieldException("The class " + UserDTO.class + " has not filed " + field);
                }
            }
        }
        //execute query
        List<User>userList = userRepository.findByIdIn(idList);
        //set lazy fetch to null
        userList.forEach(u -> discoverLazyFetch.discoverAndSetToNull(u));
        //map and send
        if(fieldSet != null) {
            List<UserDTO>userDTOs = new ArrayList<>();
            userList.forEach(u -> userDTOs.add(mapper.map(u, UserDTO.class, "full")));
            List<CustomFields>customFieldses = new ArrayList<>();
            userDTOs.forEach(u -> customFieldses.add(new CustomFields(u, fieldSet)));
            return Response.ok(customFieldses).build();
        } else {
            return Response.ok(userList).build();
        }
    }

    @Path("{id}/email")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEmail(@Pattern(regexp = "^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*\n@[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$;") String email,
                                @PathParam("id") long id) {
        User user = userRepository.findById(id);
        if(user == null) {
            throw new NoResultException("User has not been founded with id " + id);
        }
        user.setEmail(email);
        userService.update(user);
        URI location = uriInfo.getBaseUriBuilder().path(UserResource.class).path(String.valueOf(id)).build();
        return Response.status(Response.Status.SEE_OTHER).location(location).build();
    }

    @Path("{id}/phone")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePhone(@Pattern(regexp = "([0-9]{9})|([0-9]{3}-[0-9]{3}-[0-9]{3})|([0-9]{3} [0-9]{3} [0-9]{3})") String phone,
                                @PathParam("id") long id) {
        User user = userRepository.findById(id);
        if(user == null) {
            throw new NoResultException("User has not been founded with id " + id);
        }
        user.setPhone(phone);
        userService.update(user);
        URI location = uriInfo.getBaseUriBuilder().path(UserResource.class).path(String.valueOf(id)).build();
        return Response.status(Response.Status.SEE_OTHER).location(location).build();
    }

    @Path("{id}/settings")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSettings(@Valid UserSetting userSetting,
                                   @PathParam("id") long id) {
        User user = userRepository.findById(id);
        if(user == null) {
            throw new NoResultException("User has not been founded with id " + id);
        }
        user.setUserSetting(userSetting);
        userService.update(user);
        URI location = uriInfo.getBaseUriBuilder().path(UserResource.class).path(String.valueOf(id)).build();
        return Response.status(Response.Status.SEE_OTHER).location(location).build();
    }


    @Path("{id}/avatar")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAvatar(MultipartFormDataInput input) {
        //TODO to implement
//        Map<String, List<InputPart>> inputParts = input.getFormDataMap();
//        List<InputPart>imageInputParts =  inputParts.get("image");
//        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        throw new NotImplementedException();
    }
}
