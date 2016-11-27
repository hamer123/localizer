package com.pw.localizer.inceptor;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.logging.Logger;

import java.io.Serializable;

@Interceptor
@Loggable
public class LoggingInterceptor implements Serializable {

	@Inject
	private Logger logger;
	
	@AroundInvoke
	public Object logMethod(InvocationContext ic)throws Exception{
		StringBuilder logBuilder = new StringBuilder();
		logBuilder.append(ic.getTarget().getClass().getSuperclass())
				.append(" method ")
				.append(ic.getMethod().getName());
		logger.info("before " + logBuilder.toString());
		try {
			return ic.proceed();
		} finally {
			logger.info("after " + logBuilder.toString());
		}
	}
}
