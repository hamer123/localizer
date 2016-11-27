package com.pw.localizer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang.reflect.FieldUtils;


import java.io.IOException;

/**
 * Created by Patryk on 2016-10-29.
 */

public class CustomFieldsSerialize extends JsonSerializer<CustomFields> {

    @Override
    public void serialize(CustomFields customFields, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try{
            jsonGenerator.writeStartObject();
            for(String fieldName : customFields.getFieldNames()) {
                serializerProvider.defaultSerializeField(fieldName, FieldUtils.readField(customFields.getResource(), fieldName, true), jsonGenerator);
            }
            jsonGenerator.writeEndObject();
        }  catch (IllegalAccessException e) {
            //TODO
        }
    }
}
