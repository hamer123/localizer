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
public class DurationInterceptor implements Serializable {

    @Inject
    private Logger logger;

    @AroundInvoke
    public Object logMethod(InvocationContext ic)throws Exception{
        long time = System.currentTimeMillis();
        Object result = ic.proceed();
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(ic.getTarget().getClass().getSuperclass())
                .append(" method ")
                .append(ic.getMethod().getName())
                .append(" duration time ")
                .append(System.currentTimeMillis() - time)
                .append("ms");
        logger.info(logBuilder.toString());
        return result;
    }
}
