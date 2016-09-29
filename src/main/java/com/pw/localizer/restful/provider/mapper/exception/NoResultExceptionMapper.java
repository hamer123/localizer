package com.pw.localizer.restful.provider.mapper.exception;

import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Patryk on 2016-09-28.
 */

@Provider
public class NoResultExceptionMapper implements ExceptionMapper<NoResultException> {

    @Override
    public Response toResponse(NoResultException exception) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
