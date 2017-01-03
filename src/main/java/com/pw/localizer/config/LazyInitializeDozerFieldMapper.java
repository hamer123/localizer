package com.pw.localizer.config;

import org.dozer.CustomFieldMapper;
import org.dozer.classmap.ClassMap;
import org.dozer.fieldmap.FieldMap;
import org.hibernate.collection.internal.PersistentSet;


public class LazyInitializeDozerFieldMapper implements CustomFieldMapper {
    public boolean mapField(Object source, Object destination, Object sourceFieldValue, ClassMap classMap, FieldMap fieldMapping) {

        // Check if field is a Hibernate PersistentSet
        if (!(sourceFieldValue instanceof PersistentSet
                || sourceFieldValue instanceof org.hibernate.collection.internal.PersistentBag)) {
            // Allow dozer to map as normal
            return false;
        }

        destination = null;
        return true;
    }
}