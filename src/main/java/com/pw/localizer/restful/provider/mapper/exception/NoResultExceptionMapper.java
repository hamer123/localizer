package com.pw.localizer.restful.provider.mapper.exception;

import com.pw.localizer.model.general.ApiError;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoResultExceptionMapper implements ExceptionMapper<NoResultException> {

    @Override
    public Response toResponse(NoResultException exception) {
        ApiError error = new ApiError(Response.Status.NOT_FOUND, "Resource has not been founded", exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}
