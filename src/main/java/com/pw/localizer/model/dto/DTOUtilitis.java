package com.pw.localizer.model.dto;

import org.hibernate.LazyInitializationException;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by Patryk on 2016-09-28.
 */
public class DTOUtilitis {

    public static <T> T convertHibernateProxyToNull(T obj){
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields){
            try {
                field.setAccessible(true);
                Object objField = field.get(obj);
                try{
                    if(objField != null){
                        System.out.println(objField.getClass());
                        //do something
                        objField.equals(null);
//                        if(objField instanceof PersistentCollection)
//                            System.out.println("WE GOT IT !" + field.getName());
                    }
                }catch (LazyInitializationException e){
                    //if throw lazy set to null
                    System.out.println("WE GOT EXCEPTION !" + field.getName());
                    field.set(obj,null);
                } catch(Throwable e){
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch(Throwable e){
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static <U,T extends Collection<U>> T convertCollectionHibernateProxyToNull(T collection){
        for(U obj : collection){
            DTOUtilitis.convertHibernateProxyToNull(obj);
        }
        return collection;
    }
}
