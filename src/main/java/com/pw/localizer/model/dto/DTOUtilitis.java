package com.pw.localizer.model.dto;

import org.hibernate.LazyInitializationException;
import org.hibernate.collection.internal.AbstractPersistentCollection;
import javax.persistence.Entity;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

public class DTOUtilitis {

    public static  <T> T convertHibernateProxyToNull(T obj){
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields){
            try {
                field.setAccessible(true);
                Object objField = field.get(obj);
                try{
                    if(objField != null){
                        System.out.println(objField.getClass());

                        Class<?>classType = objField.getClass();
                        //do something
                        objField.hashCode();
                        System.out.println(objField.getClass().getDeclaredFields().length);

                        for(Annotation annotation : objField.getClass().getAnnotations()) {
                            System.out.println(annotation);
                            if(annotation instanceof Entity){
                                convertHibernateProxyToNull(objField);
                                System.out.println("!!!!!!!!!!!!!!!!!");
                            }
                        }

                        if(objField instanceof Collection){
                            Collection collection = (Collection) objField;
//                            convertCollectionHibernateProxyToNull(collection);
                        } else if(objField instanceof AbstractPersistentCollection){
                            AbstractPersistentCollection collection = (AbstractPersistentCollection)objField;
//                            convertCollectionHibernateProxyToNull(collection);
                            System.out.println("WHAT TO DO?");
                        }

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

    private static <U,T extends Collection<U>> T convertCollectionHibernateProxyToNull(T collection){
        for(U obj : collection){
            convertHibernateProxyToNull(obj);
        }
        return collection;
    }
}
