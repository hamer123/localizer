package com.pw.localizer.restful.resource.location;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pw.localizer.security.restful.Secured;
import org.jboss.logging.Logger;
import com.pw.localizer.model.session.RestSession;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.service.location.LocationService;

@Secured
@Path("/location/gps")
public class RestLocationGPS {
	@Inject
	private LocationService locationService;
	@Inject
	Logger logger;
	
	@POST()
	@Path("/create")
	@Consumes( value = {MediaType.APPLICATION_JSON} )
	@Produces( value = {MediaType.APPLICATION_JSON} )
	public Response createLocation(LocationGPS locationGPS , @Context RestSession restSession){
		try{
			long id = restSession.getUser().getId();
			locationService.createLocationGPSUpdateUserCurrentLocationGPS(locationGPS, id);
			return Response.status(Response.Status.CREATED).build();
		}catch(Exception e){
			logger.error(" Nie ulado sie zapisac lokalizacji..." + e);
			return Response.status( Response.Status.EXPECTATION_FAILED )
					.build();
		}
	}
	
}
