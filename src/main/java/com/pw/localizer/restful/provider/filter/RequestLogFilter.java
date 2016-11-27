package com.pw.localizer.restful.provider.filter;

import org.apache.commons.collections.map.MultiValueMap;
import org.jboss.logging.Logger;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by Patryk on 2016-11-06.
 */

@Provider
@LogRequest
@Priority(Priorities.USER)
public class RequestLogFilter implements ContainerRequestFilter {
//    @Context
//    private UriInfo uriInfo;
//
//    @Context
//    private HttpServletRequest httpServletRequest;
    @Inject
    private Logger logger;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        StringBuilder info = new StringBuilder();
        info.append("\n").append("URL: ").append(requestContext.getUriInfo().getAbsolutePath()).append("\n")
                .append("Method: ").append(requestContext.getMethod()).append("\n")
                .append("Media-Type: ").append(requestContext.getMediaType()).append("\n");
        if(requestContext.getHeaderString("Authorization") != null) {
            info.append("Authorization: ").append(requestContext.getHeaderString("Authorization").replaceAll("Bearer ", "")).append("\n");
        }
        if(requestContext.getSecurityContext().getUserPrincipal() != null) {
            info.append("Login: ").append(requestContext.getSecurityContext().getUserPrincipal().getName()).append("\n");
        }
        MultivaluedMap<String, String> queryParams = requestContext.getUriInfo().getQueryParameters();
        info.append("Params").append("\n");
        for(String key : queryParams.keySet()) {
            queryParams.get(key).forEach(value -> info.append(key).append(": ").append(value).append("\n"));
        }
        logger.info(info.toString());
    }
}
