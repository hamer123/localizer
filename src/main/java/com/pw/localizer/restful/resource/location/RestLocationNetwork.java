package com.pw.localizer.restful.resource.location;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.pw.localizer.inceptor.ErrorLog;
import com.pw.localizer.model.dto.LocationNetworkDTO;
import com.pw.localizer.security.restful.Secured;
import org.jboss.logging.Logger;

import com.pw.localizer.model.session.RestSession;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.repository.location.LocationNetworkRepository;
import com.pw.localizer.service.location.LocationService;
import com.pw.localizer.inceptor.LoggingInterceptor;

@Secured
@Path("/locations/network")
@Interceptors(value = LoggingInterceptor.class)
public class RestLocationNetwork {
	@Inject
	private LocationService locationService;
	@Inject
	private LocationNetworkRepository LocationNetworkRepository;

	@ErrorLog
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createLocation(LocationNetworkDTO locationNetworkDTO, @Context UriInfo uriInfo){

		//TODO
		return null;
	}

	@ErrorLog
	@GET
	@Produces( value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	public Response findLocation(@NotNull @QueryParam("login") String login,
								 @NotNull @QueryParam("fromDate") String fromDate,
								 @NotNull @QueryParam("toDate") String toDate,
								 @DefaultValue("1000") @Max(1000) @QueryParam("maxRecords")int maxRecords){
		//TODO
		return null;
	}
}
