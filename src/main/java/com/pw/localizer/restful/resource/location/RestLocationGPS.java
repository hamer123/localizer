package com.pw.localizer.restful.resource.location;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.pw.localizer.inceptor.ErrorLog;
import com.pw.localizer.model.dto.LocationGPSDTO;
import com.pw.localizer.model.query.LocationSearch;
import com.pw.localizer.repository.location.LocationGPSRepository;
import com.pw.localizer.security.restful.Secured;
import com.pw.localizer.model.session.RestSession;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.service.location.LocationService;
import org.dozer.Mapper;
import org.jboss.resteasy.links.AddLinks;
import org.jboss.resteasy.links.LinkResource;
import org.jboss.resteasy.specimpl.LinkImpl;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Secured
@Path("/location/gps")
public class RestLocationGPS {
	@Inject
	private LocationService locationService;
	@Inject
	private LocationGPSRepository locationGPSRepository;
	@Inject
	private Mapper mapper;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocation(@PathParam("id")Long id){
		LocationGPS locationGPS = locationGPSRepository.findById(id);
		return Response.ok(mapper.map(locationGPS,LocationGPSDTO.class)).build();
	}


	@ErrorLog
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createLocation(LocationGPSDTO locationGPSDTO, @Context UriInfo uri){
		LocationGPS locationGPS = mapper.map(locationGPSDTO,LocationGPS.class);
		locationGPS = locationService.createLocationGPS(locationGPS);
		UriBuilder uriBuilder = uri.getAbsolutePathBuilder();
		URI sourceURI = uriBuilder.path(String.valueOf(locationGPS.getId())).build();
//		Link self = Link.fromUri(sourceURI)
//				.rel("self")
//				.title("self")
//				.type(MediaType.APPLICATION_JSON).build();
		return Response.status(Response.Status.CREATED)
//				.entity(mapper.map(locationGPS,LocationGPSDTO.class))
				.location(sourceURI)
				.build();
	}

	@ErrorLog
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocation(@NotNull @QueryParam("login") String login,
								@NotNull @QueryParam("fromDate") String fromDate,
								@NotNull @QueryParam("toDate") String toDate,
								@DefaultValue("1000") @Max(1000) @QueryParam("maxRecords")int maxRecords){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			Date from = sdf.parse(fromDate);
			Date to = sdf.parse(toDate);
			List<LocationGPS>locationGpsList = locationGPSRepository.findByLoginAndDateOrderByDate(login,from,to,maxRecords);
			List<LocationGPSDTO>locationGPSDTOList = locationGpsList.stream()
					.map(l -> mapper.map(l,LocationGPSDTO.class))
					.collect(Collectors.toList());
			return Response.ok(locationGPSDTOList).build();
		} catch (ParseException e) {
			throw new WebApplicationException(e, Response.status(Response.Status.BAD_REQUEST).entity("Not supported data format, allowed format is dd-MM-yyyy hh:mm:ss ").build());
		}
	}
}
