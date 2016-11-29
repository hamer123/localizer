package com.pw.localizer.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;
import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.general.MailMessage;
import com.pw.localizer.service.area.event.AreaEventService;
import org.jboss.logging.Logger;
import com.pw.localizer.service.message.area.MailMessageService;

@Singleton
public class AreaMailWorker {
	private static final int EXECUTOR_SERVICE_POOL_SIZE = 25;
	@Resource
	private ManagedExecutorService executorService;
	@Inject
	private MailMessageService mailMessageService;
	@Inject
	private AreaEventService areaEventService;
	@Inject
	private Logger logger;

	@Schedule(minute="*/1", hour="*", persistent = false)
	public void sendMails() {
		Collection<Callable<Boolean>> tasks;
		List<AreaEvent>areaEvents = getAreaEvents();
		for(int i = 0; i < areaEvents.size(); i++){
			tasks = new ArrayList<>();
			//create tasks
			for(int count = 0; count < EXECUTOR_SERVICE_POOL_SIZE && i < areaEvents.size(); count++, i++) {
			    tasks.add(createTask(areaEvents.get(i)));
			}
			//execute tasks
			try {
				executorService.invokeAll(tasks);
			} catch (InterruptedException exception) {
				logger.error(exception);
			}
		}
		logger.info("Count of emails " + areaEvents.size());
	}

	private Callable<Boolean> createTask(AreaEvent areaEvent) {
		return () -> {
		    //create message
			MailMessage mailMessage = new MailMessage(
					areaEvent.getArea().getProvider().getEmail(),
					String.format("Event obszaru śledzenia %s", areaEvent.getArea().getName()),
					areaEvent.getMessage());
			//send
			try{
				mailMessageService.send(mailMessage);
				areaEvent.setSendMail(false);
			} catch(Exception exception){
				//increment attempt
                areaEvent.setAttemptToSend(areaEvent.getAttemptToSend() + 1);
                logger.error(exception);
				return false;
			}
			return true;
		};
	}

	private List<AreaEvent> getAreaEvents(){
		return areaEventService.getBySendMailAndAttemptToSendLowerThan(true, 3);
	}

	@AroundTimeout
	public Object logSendingMails(InvocationContext ic) throws Exception{
		long time = System.currentTimeMillis();
		Object result;
		try {
			result = ic.proceed();
			return result;
		} finally {
			logger.info("job has ended after " + (System.currentTimeMillis() - time) + "ms");
		}
	}
}
