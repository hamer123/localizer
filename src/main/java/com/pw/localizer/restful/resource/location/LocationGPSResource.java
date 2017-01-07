package com.pw.localizer.restful.resource.location;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import com.pw.localizer.inceptor.ErrorLog;
import com.pw.localizer.model.dto.LocationGpsDTO;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.qualifier.GPS;
import com.pw.localizer.repository.location.LocationGPSRepository;
import com.pw.localizer.repository.user.UserRepository;
import com.pw.localizer.restful.provider.filter.LogEntityRequest;
import com.pw.localizer.restful.provider.filter.LogRequest;
import com.pw.localizer.security.restful.Secured;
import com.pw.localizer.model.entity.LocationGPS;
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
public class LocationGPSResource {
	@Inject
	private UserRepository userRepository;
	@Inject @GPS
	private LocationService<LocationGPS> locationService;
	@Inject
	private LocationGPSRepository locationGPSRepository;
	@Inject
	private Mapper mapper;

	@GET
	@Path("/{userId}/locations/gps/{locationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocation(@PathParam("locationId")Long id){
		LocationGPS locationGPS = locationGPSRepository.findById(id);
		return Response.ok(mapper.map(locationGPS,LocationGpsDTO.class)).build();
	}

	@ErrorLog
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{userId}/lastLocations/gps")
	public Response createLocation(@PathParam("userId")Long userId,
			                       @Valid LocationGpsDTO locationGpsDTO,
								   @Context UriInfo uri){
		User user = userRepository.findById(userId);
		if(user == null) {
			throw new NoResultException("User has not been founded with id " + userId);
		}
		LocationGPS locationGPS = mapper.map(locationGpsDTO,LocationGPS.class);
		locationGPS.setUser(user);
		locationGPS = (LocationGPS) locationService.create(locationGPS);
//		UriBuilder uriBuilder = uri.getAbsolutePathBuilder();
//		URI sourceURI = uriBuilder.path(String.valueOf(locationGPS.getId())).build();
//		Link self = Link.fromUri(sourceURI)
//				.rel("self")
//				.title("self")
//				.type(MediaType.APPLICATION_JSON).build();
		return Response.status(Response.Status.OK)
//				.location(sourceURI)
				.entity(mapper.map(locationGPS, LocationGpsDTO.class))
				.build();
	}

	@ErrorLog
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/locations/gps/search/findByUserLoginAndDateBetween")
	public Response getLocation(@NotNull @QueryParam("login") String login,
								@NotNull @QueryParam("fromDate") String fromDate,
								@NotNull @QueryParam("toDate") String toDate,
								@DefaultValue("1000") @Max(1000) @QueryParam("maxRecords")int maxRecords){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			Date from = sdf.parse(fromDate);
			Date to = sdf.parse(toDate);
			List<LocationGPS>locationGpsList = locationGPSRepository.findByLoginAndDateOrderByDate(login,from,to,maxRecords);
			List<LocationGpsDTO> locationGpsDTOList = locationGpsList.stream()
					.map(l -> mapper.map(l,LocationGpsDTO.class))
					.collect(Collectors.toList());
			return Response.ok(locationGpsDTOList).build();
		} catch (ParseException e) {
			throw new WebApplicationException(e, Response.status(Response.Status.BAD_REQUEST).entity("Not supported data format, allowed format is dd-MM-yyyy hh:mm:ss ").build());
		}
	}
}
