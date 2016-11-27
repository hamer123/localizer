package com.pw.localizer.restful.resource.security;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pw.localizer.model.dto.DTOUtilitis;
import com.pw.localizer.model.dto.UserDTO;
import com.pw.localizer.model.security.Credentials;
import com.pw.localizer.model.session.RestSession;
import com.pw.localizer.repository.user.UserRepository;
import com.pw.localizer.restful.provider.filter.LogRequest;
import com.pw.localizer.security.restful.AuthenticateException;
import com.pw.localizer.security.restful.AuthenticateService;
import com.pw.localizer.security.restful.DatabaseAuthenticat;
import com.pw.localizer.security.restful.Secured;
import org.dozer.Mapper;
import org.jboss.logging.Logger;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.singleton.RestSessionManager;

@Path("authentication")
public class SecurityResource {
	@Inject @DatabaseAuthenticat
	private AuthenticateService authenticateService;
	@Inject
	private UserRepository userRepository;
	@Inject
	private RestSessionManager restSessionManager;
	@Inject
	private Mapper mapper;

	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@BeanParam Credentials credentials){
		if(authenticateService.authenticate(credentials.getLogin(), credentials.getPassword())){
			User user = userRepository.findByLoginEagerFetchAll(credentials.getLogin());
			RestSession restSession = restSessionManager.addSession(user);
			UserDTO userDTO = mapper.map(user,UserDTO.class, "full");
			return Response.ok()
					.entity(userDTO)
					.header("X-Auth-Token",restSession.getToken())
					.build();
		} else {
			return Response.status( Response.Status.UNAUTHORIZED )
					.entity("Invalid login or password")
					.build();
		}
	}
	
//	@Path("logout")
//	@POST
//	@Secured
//	public Response logout(@HeaderParam("Authenticate")String token){
//		try{
//			token = extractToken(token);
//			boolean result = restSessionManager.invalidationRestSession(token);
//			if(result == true){
//				return Response.status(Response.Status.OK)
//						       .build();
//			}
//		} catch(Exception e) {
//			logger.error(" Nie udane wylogowanie dla [ token: " + token + " ]" + e.getMessage());
//		}
//		return Response.serverError().build();
//	}
//
//	private String extractToken(String authorizationHeader){
//		return authorizationHeader.substring("Bearer".length()).trim();
//	}
}
