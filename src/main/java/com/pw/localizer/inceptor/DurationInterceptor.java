package com.pw.localizer.inceptor;

import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

/**
 * Created by wereckip on 16.09.2016.
 */
@Interceptor @DurationLogging
public class DurationInterceptor implements Serializable{
    @Inject
    private Logger logger;

    @AroundInvoke
    public Object logMethod(InvocationContext ic)throws Exception{
        long time = System.currentTimeMillis();
        Object result = ic.proceed();
        logger.info(ic.getTarget().toString() + " " + ic.getMethod().getName() + "() duration time "
                + (System.currentTimeMillis() - time) + " ms");
        return result;
    }
}
