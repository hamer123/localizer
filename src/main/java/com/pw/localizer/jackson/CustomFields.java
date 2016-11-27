package com.pw.localizer.jackson;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Set;

/**
 * Created by Patryk on 2016-10-29.
 */

@JsonSerialize(using = CustomFieldsSerialize.class)
public class CustomFields {

    private Object resource;
    private Set<String> fieldNames;

    public CustomFields(Object resource, Set<String> fieldNames) {
        this.resource = resource;
        this.fieldNames = fieldNames;
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }

    public Set<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(Set<String> fieldNames) {
        this.fieldNames = fieldNames;
    }
}
