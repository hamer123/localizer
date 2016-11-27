package com.pw.localizer.job;

import java.util.Date;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.jboss.logging.Logger;
import com.pw.localizer.model.session.RestSession;
import com.pw.localizer.singleton.RestSessionManager;

@Singleton
public class InvalidateRestSession {
	@Inject
	private RestSessionManager restSessionManager;
	@Inject
	private Logger logger;
	
	@Schedule(minute="*/1",hour="*", persistent=false)
	public void timeoutRestSession(){
		logger.info("invalidate timeout rest session job has started");
		for(String token : restSessionManager.tokens()) {
			RestSession restSession = restSessionManager.getSession(token);
			if(isPassedTime(restSession.getLastUsed())) {
				restSessionManager.invalidationRestSession(token);
			}
		}
	}
	
	boolean isPassedTime(Date date){
		final long threeMinute = 3 * 60 * 1000;
		long currentTime = new Date().getTime();
		
		return currentTime - threeMinute > date.getTime();
	}
}
