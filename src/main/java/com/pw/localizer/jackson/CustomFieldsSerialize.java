package com.pw.localizer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang.reflect.FieldUtils;
import org.jboss.logging.Logger;
import javax.inject.Inject;
import java.io.IOException;

public class CustomFieldsSerialize extends JsonSerializer<CustomFields> {
    @Inject
    private Logger logger;

    @Override
    public void serialize(CustomFields customFields, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try{
            jsonGenerator.writeStartObject();
            for(String fieldName : customFields.getFieldNames()) {
                serializerProvider.defaultSerializeField(fieldName, FieldUtils.readField(customFields.getResource(), fieldName, true), jsonGenerator);
            }
            jsonGenerator.writeEndObject();
        }  catch (IllegalAccessException e) {
            //should never occur
            logger.error("Should never occur! something is wrong", e);
        }
    }
}
