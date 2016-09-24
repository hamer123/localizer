package com.pw.localizer.restful.resource.location;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	@Inject
	private Logger logger;
	
	@POST
	@Consumes( value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	@Produces( value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	public Response createLocation(LocationNetwork locationNetwork, @Context RestSession restSession){
		long id = restSession.getUser().getId();
		locationService.createLocationNetworkUpdateUserCurrentLocationNetwork(locationNetwork, id);
		return Response.status(Response.Status.OK).build();
	}
	
	@GET
	@Path("{id}")
	@Produces( value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	public Response findLocation(@PathParam("id") long id){
		try{
			Location location = LocationNetworkRepository.findById(id);
			return Response.status( Response.Status.OK )
					       .entity(location)
					       .build();
		}catch(Exception e){
			return Response.status( Response.Status.NOT_FOUND )
					       .build();
		}
	}

}
