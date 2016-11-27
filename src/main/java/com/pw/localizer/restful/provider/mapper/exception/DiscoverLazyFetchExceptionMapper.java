package com.pw.localizer.restful.provider.mapper.exception;

import com.pw.localizer.exception.DiscoverLazyFetchException;
import com.pw.localizer.model.general.ApiError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by Patryk on 2016-11-04.
 */
public class DiscoverLazyFetchExceptionMapper implements ExceptionMapper<DiscoverLazyFetchException> {

    @Override
    public Response toResponse(DiscoverLazyFetchException exception) {
        ApiError error = new ApiError(Response.Status.INTERNAL_SERVER_ERROR, "Server Error", exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }
}
