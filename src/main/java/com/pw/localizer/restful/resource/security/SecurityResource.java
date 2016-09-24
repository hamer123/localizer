package com.pw.localizer.restful.resource.security;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pw.localizer.model.security.Credentials;
import com.pw.localizer.model.session.RestSession;
import com.pw.localizer.security.restful.AuthenticateService;
import com.pw.localizer.security.restful.DatabaseAuthenticat;
import com.pw.localizer.security.restful.Secured;
import org.jboss.logging.Logger;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.singleton.RestSessionManager;

@Path("authentication")
public class SecurityResource {
	public static final String LOGIN = "/login";
	public static final String LOGOUT = "/logout";
	@Inject
	private RestSessionManager restSessionManager;
	@Inject
	private Logger logger;
	@Inject @DatabaseAuthenticat
	private AuthenticateService authenticateService;
	
	@POST
	@Path(LOGIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@BeanParam Credentials credentials){
		try{
			User user = authenticateService.authenticate(credentials.getLogin(), credentials.getPassword());
			RestSession restSession = restSessionManager.addSession(user);
			return Response.ok()
					.entity(user)
					.header("X-Auth-Token",restSession.getToken())
					.build();
		}catch(NoResultException nre){
			return Response.status( Response.Status.UNAUTHORIZED )
					       .entity("Konto o podanych parametrach nie istnieje !")
					       .build();
		}catch(Exception e){
			logger.error(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					       .build();
		}
	}
	
	@Path(LOGOUT)
	@GET
	@Secured
	public Response logout(@HeaderParam("Authenticate")String token){
		try{
			token = extractToken(token);
			boolean result = restSessionManager.invalidationRestSession(token);
			if(result == true){
				return Response.status( Response.Status.OK )
						       .build();
			}
		} catch(Exception e) {
			logger.error(" Nie udane wylogowanie dla [ token: " + token + " ]" + e.getMessage());
		}
		return Response.serverError().build();
	}

	private String extractToken(String authorizationHeader){
		return authorizationHeader.substring("Bearer".length()).trim();
	}

}
