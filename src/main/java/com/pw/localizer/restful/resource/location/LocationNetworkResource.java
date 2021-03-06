package com.pw.localizer.restful.resource.location;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.pw.localizer.inceptor.ErrorLog;
import com.pw.localizer.model.dto.LocationNetworkDTO;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.qualifier.Network;
import com.pw.localizer.repository.user.UserRepository;
import com.pw.localizer.restful.provider.filter.LogEntityRequest;
import com.pw.localizer.restful.provider.filter.LogRequest;
import com.pw.localizer.security.restful.Secured;
import com.pw.localizer.repository.location.LocationNetworkRepository;
import com.pw.localizer.service.location.LocationService;
import org.dozer.Mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@LogRequest
@LogEntityRequest
@Path("/users")
@Secured
public class LocationNetworkResource {
	@Inject
	private UserRepository userRepository;
	@Inject @Network
	private LocationService<LocationNetwork> locationService;
	@Inject
	private LocationNetworkRepository locationNetworkRepository;
	@Inject
	private Mapper mapper;

	@GET
	@Path("/{userId}/locations/network/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocation(@PathParam("id")Long id){
		LocationNetwork locationNetwork = locationNetworkRepository.findById(id);
		return Response.ok(mapper.map(locationNetwork, LocationNetworkDTO.class)).build();
	}

	@ErrorLog
	@POST
	@Path("/{userId}/lastLocations/network/obcy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createLocation(@PathParam("userId")Long userId,
								   @Valid LocationNetworkDTO locationNetworkDTO,
								   @Context UriInfo uriInfo){
		User user = userRepository.findById(userId);
		if(user == null) {
			throw new NoResultException("User has not been founded with id " + userId);
		}
		LocationNetwork locationNetwork = mapper.map(locationNetworkDTO, LocationNetwork.class);
		locationNetwork.setUser(user);
		locationNetwork = (LocationNetwork) locationService.create(locationNetwork);
		return Response.ok(mapper.map(locationNetwork, LocationNetworkDTO.class)).build();
	}

	@ErrorLog
	@GET
	@Path("/locations/network/search/findByUserLoginAndLocalizationServiceAndDateBetween")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findLocation(@NotNull @QueryParam("login") String login,
								 @NotNull @QueryParam("fromDate") String fromDate,
								 @NotNull @QueryParam("toDate") String toDate,
								 @NotNull @QueryParam("service")LocalizationService localizationService,
								 @DefaultValue("1000")@Max(1000)@QueryParam("maxRecords")int maxRecords){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			Date from = sdf.parse(fromDate);
			Date to = sdf.parse(toDate);
			List<LocationNetwork>locationNetworks = null;
			if(localizationService == LocalizationService.NASZ){
				locationNetworks = locationNetworkRepository.findByLoginAndDateForServiceNaszOrderByDate(login,from,to,maxRecords);
			} else {
				locationNetworks = locationNetworkRepository.findByLoginAndDateForServiceObcyOrderByDate(login,from,to,maxRecords);
			}
			List<LocationNetworkDTO>locationNetworkDTOs = locationNetworks.stream()
					.map(l -> mapper.map(l,LocationNetworkDTO.class))
					.collect(Collectors.toList());
			return Response.ok(locationNetworkDTOs).build();
		} catch (ParseException e) {
			throw new WebApplicationException(e, Response.status(Response.Status.BAD_REQUEST).entity("Not supported data format, allowed format is dd-MM-yyyy hh:mm:ss ").build());
		}
	}
}
