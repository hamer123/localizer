package com.pw.localizer.security.restful;
import com.pw.localizer.model.session.RestSession;
import com.pw.localizer.model.enums.Role;
import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Provider
@Secured
@Priority(Priorities.AUTHORIZATION)
public class RestAuthorizationFilter implements ContainerRequestFilter{
	@Context 
	private HttpServletRequest request;
	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(!isLoginRequest(request)){
			RestSession restSession = (RestSession) request.getAttribute("rest_session");
			restSession.setLastUsed(new Date());

			// Get the resource class which matches with the requested URL
			// Extract the roles declared by it
			Class<?> resourceClass = resourceInfo.getResourceClass();
			List<Role> classRoles = extractRoles(resourceClass);

			// Get the resource method which matches with the requested URL
			// Extract the roles declared by it
			Method resourceMethod = resourceInfo.getResourceMethod();
			List<Role> methodRoles = extractRoles(resourceMethod);

			// Check if there are annotations on method or class, if they are check user permission
			try{
				if(!methodRoles.isEmpty()){
					checkPermissions(restSession,methodRoles);
				} else {
					if(!classRoles.isEmpty())
						checkPermissions(restSession,classRoles);
				}

				// Add SecurityContext to JAX-RS context
				requestContext.setSecurityContext(new SecurityContextRestful(restSession));
			} catch(Exception e){
				requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
			}

		}
	}

	private void checkPermissions(RestSession restSession, List<Role>roles) throws Exception {
		for(Role role : roles)
			if(restSession.isInRole(role)) return;

		throw new Exception("Authorization has been failed");
	}

	private boolean isLoginRequest(HttpServletRequest request){
		String requestURL = request.getRequestURI();
		return requestURL.endsWith("/login");
	}

	//Extract the roles from the annotated element
	private List<Role> extractRoles(AnnotatedElement annotatedElement) {
		if (annotatedElement == null) {
			return new ArrayList<Role>();
		} else {
			Secured secured = annotatedElement.getAnnotation(Secured.class);
			if (secured == null) {
				return new ArrayList<Role>();
			} else {
				Role[] allowedRoles = secured.value();
				return Arrays.asList(allowedRoles);
			}
		}
	}

	private class SecurityContextRestful implements SecurityContext{
		private List<Role>roles = new ArrayList<>();
		private String login;

		public SecurityContextRestful(RestSession restSession){
			this.login = restSession.getUser().getLogin();
			this.roles = restSession.getUser().getRoles();
		}

		@Override
		public Principal getUserPrincipal() {
			return new Principal() {
				@Override
				public String getName() {return login; }
			};
		}

		@Override
		public boolean isUserInRole(String role) {
			for(Role _role : roles)
				if(_role.name().equalsIgnoreCase(role))
					return true;
			return false;
		}

		@Override
		public boolean isSecure() {
			return request.isSecure();
		}

		@Override
		public String getAuthenticationScheme() {
			return "DIGEST_AUTH ";
		}
	}
}
