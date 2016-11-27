package com.pw.localizer.jackson;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by Patryk on 2016-10-29.
 */

@ApplicationScoped
public class CustomFieldsValidation {

    /** Only validate field name and return boolean */
    public boolean validateField(Class<?> c, String fieldName){
        try{
            c.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
