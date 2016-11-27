package com.pw.localizer.restful.provider.filter;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Patryk on 2016-11-07.
 */

@Provider
@LogEntityRequest
@Priority(Priorities.USER)
public class RequestEntityLog implements ContainerRequestFilter {

    @Inject
    private Logger LOG;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (!"POST".equals(requestContext.getMethod())
                || !MediaType.APPLICATION_JSON_TYPE.equals(requestContext.getMediaType())
                || requestContext.getEntityStream() == null) {
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(requestContext.getEntityStream(), baos);
        byte[] bytes = baos.toByteArray();
        LOG.info("Posted: " + new String(bytes, "UTF-8"));
        requestContext.setEntityStream(new ByteArrayInputStream(bytes));
    }
}
