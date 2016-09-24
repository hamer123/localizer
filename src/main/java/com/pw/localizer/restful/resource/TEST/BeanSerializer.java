package com.pw.localizer.restful.resource.TEST;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by Patryk on 2016-09-24.
 */

public class BeanSerializer extends JsonSerializer<Bean> {

    @Override
    public void serialize(Bean bean, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name1",bean.getName());
        jsonGenerator.writeEndObject();
    }
}
