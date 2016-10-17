package com.pw.localizer.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.utilities.MailMessage;
import org.jboss.logging.Logger;
import com.pw.localizer.exception.AreaMialMessageException;
import com.pw.localizer.repository.area.event.AreaEventGPSRepository;
import com.pw.localizer.repository.area.event.AreaEventNetworkRepository;
import com.pw.localizer.service.message.area.MailMessageService;

@Stateless
public class AreaMailWorker {
	private static final int POOL_SIZE = 25;

	@Inject
	private ManagedExecutorService executorService;
	@Inject
	@com.pw.localizer.qualifier.AreaMailMessage
	private MailMessageService areaSendMessage;
	@Inject
	private AreaEventGPSRepository areaEventGPSRepository;
	@Inject
	private AreaEventNetworkRepository areaEventNetworkRepository;
	@Inject
	private Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	@Schedule(minute = "*/1", hour="*", persistent = false)
	public int sendMails(){
		Collection<Callable<Void>> tasks;
		List<AreaEvent>areaEvents = getAreaEvents();
		for(int i = 0; i<areaEvents.size(); i++){
			tasks = new ArrayList<>();
			//create tasks
			for(int count = 0; count < POOL_SIZE; count++){

			}
			//execute tasks
			try {
				executorService.invokeAll(tasks);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//			sendMailAndUpdateStatus(areaEvent);
//
		return areaEvents.size();
	}
	
	private void sendMailAndUpdateStatus(AreaEvent areaEvent){
		try{
			areaSendMessage.send(areaEvent);
			updateStatus(areaEvent);
		} catch(AreaMialMessageException amme){
			logger.error(amme);
		}
	}

	private Callable<Void> createTask(AreaEvent areaEvent){
		return () -> {
			MailMessage mailMessage = new MailMessage(
					areaEvent.getArea().getProvider().getEmail(),
					"topic", //TODO
					areaEvent.getMessage());
			//send
			try{
				areaSendMessage.send(mailMessage);
				areaEvent.setMailSend(true);
				entityManager.merge(areaEvent);

			} catch(Exception e){
				//set count of try
			}

			return null;
		};
	}
	
	private void updateStatus(AreaEvent areaEvent){
		areaEvent.setMailSend(false);
	}

	private List<AreaEvent> getAreaEvents(){
		List<AreaEvent>areaEvents = new ArrayList<AreaEvent>();

		//TODO nazwy tych jebanych repo na np : findByMailSendIsTrue
		areaEvents.addAll(areaEventNetworkRepository.findAllWhereMailSendIsTrue());
		areaEvents.addAll(areaEventGPSRepository.findAllWhereMailSendIsTrue());
		
		return areaEvents;
	}
	
	@AroundTimeout
	public Object logMethod(InvocationContext ic) throws Exception{
		long time = System.currentTimeMillis();
		logger.info(" job has started");
		Object result = null;
		
		try{
			result = ic.proceed();
			return result;
		}finally{
			logger.info(" job has ended after "
			           + (System.currentTimeMillis() - time )
			           + "ms with sended over "
			           + result
			           + " mails");
		}

	}
}
