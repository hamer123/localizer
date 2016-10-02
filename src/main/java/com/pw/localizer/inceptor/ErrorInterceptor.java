package com.pw.localizer.inceptor;

import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Created by Patryk on 2016-10-02.
 */

@Interceptor @ErrorLog
public class ErrorInterceptor {
    @Inject
    private Logger logger;

    @AroundInvoke
    public Object logMethod(InvocationContext ic)throws Exception{
        try{
            return ic.proceed();
        } catch(Exception e){
            logger.error(e);
            throw e;
        }
    }
}
