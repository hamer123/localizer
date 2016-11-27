package com.pw.localizer.restful.provider.filter;


import org.jboss.logging.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by Patryk on 2016-11-06.
 */

@Provider
@LogResponse
@Priority(Priorities.USER)
public class ResponseLogFilter implements ContainerResponseFilter{

    @Inject
    private Logger logger;
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        //TODO
    }
}
