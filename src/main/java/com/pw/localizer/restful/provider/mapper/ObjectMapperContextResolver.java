package com.pw.localizer.restful.provider.mapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
/**
 * Created by Patryk on 2016-09-24.
 */


public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}