package com.pw.localizer.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pw.localizer.model.dto.DTOUtilitis;

import java.io.IOException;

/**
 * Created by Patryk on 2016-09-28.
 */


public class HibernateProxySerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        DTOUtilitis dtoUtilitis = new DTOUtilitis();
        dtoUtilitis.convertHibernateProxyToNull(o);
        serializerProvider.findTypedValueSerializer(o.getClass(),true,null).serialize(o,jsonGenerator,serializerProvider);
    }
}
