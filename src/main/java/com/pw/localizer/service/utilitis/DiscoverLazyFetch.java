package com.pw.localizer.service.utilitis;

import com.pw.localizer.exception.DiscoverLazyFetchException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.LazyInitializationException;
import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.Field;

/**
 * Created by Patryk on 2016-11-04.
 */

@ApplicationScoped
public class DiscoverLazyFetch {

    public <T> T discoverAndSetToNull(T obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields) {
            try {
                Object value = FieldUtils.readField(field, obj, true);
                //we have to invoke something on the field
                value.hashCode();

                //TODO discover dla pol?
            } catch (IllegalAccessException e) {
                throw new DiscoverLazyFetchException("Błąd przy próie uzyskania dostępu do pola " + field.getName() + " class " + obj.getClass().getName(), e);
            } catch (LazyInitializationException e) {
                setFieldToNull(field, obj);
            } catch(NullPointerException e) {
                // omit
            }
        }
        return obj;
    }

    private void setFieldToNull(Field field, Object obj) {
        try {
            field.set(obj, null);
        } catch (IllegalAccessException e) {
            throw new DiscoverLazyFetchException("Nie udało się przypisac polu wartości null !", e);
        }
    }
}
