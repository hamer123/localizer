package com.pw.localizer.job;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;
import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.repository.area.event.AreaEventRepository;
import com.pw.localizer.service.area.event.AreaEventService;
import org.jboss.logging.Logger;
import com.pw.localizer.factory.AreaEventFactory;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.enums.AreaFollow;
import com.pw.localizer.model.enums.AreaMailMessageMode;
import com.pw.localizer.repository.area.AreaRepository;
import com.pw.localizer.singleton.RestSessionManager;

@Singleton
public class AreaEventWorker {
	@Inject
	private RestSessionManager restSessionManager;
	@Inject
	private AreaRepository areaRepository;
	@Inject
	private AreaEventFactory areaEventFactory;
	@Inject
	private AreaEventService areaEventService;
	@Inject
	private Logger logger;

	@Schedule(minute="*/1", hour="*", persistent = false)
	public void checkActiveAreaWithOnlineTarget() {
		for(Area area : getActiveArea()) {
			if(shouldCheckArea(area)) {
				User target = area.getTarget();
				List<Location>locations = validateUserLastLocation(target);
				if(area.getAreaFollowType() == AreaFollow.INSIDE) {
					for(Location location : locations) {
						if(!area.contains(location)) {
							createAreaEvent(location, area);
						}
					}
					if(shouldChangeMessageMail(area)) {
						area.getAreaMessageMail().setAccept(false);
					}
				} else {
					for(Location location : locations) {
						if(area.contains(location)) {
							createAreaEvent(location, area);
						}
					}
					if(shouldChangeMessageMail(area)) {
						area.getAreaMessageMail().setAccept(false);
					}
				}
			}
		}
	}
	
	@AroundTimeout
	public Object log(InvocationContext ic) throws Exception {
		long time = System.currentTimeMillis();
		Object result = ic.proceed();
		logger.info("job has ended after " + (System.currentTimeMillis() - time) + "ms");
		return result;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private AreaEvent createAreaEvent(Location location, Area area){
		AreaEvent areaEvent = areaEventFactory.create(area, location);
		location.setEventCheck(true);
		return areaEventService.create(areaEvent);
	}
	
	private List<Area> getActiveArea(){
		return areaRepository.findByActive(true);
	}
	
	private List<Location> validateUserLastLocation(User user){
		List<Location>locations = new ArrayList<>();
		Location location = user.getLastLocationGPS();
		if(validateLocation(location)) locations.add(location);
		location = user.getLastLocationNetworkNaszaUsluga();
		if(validateLocation(location)) locations.add(location);
		location = user.getLastLocationNetworObcaUsluga();
		if(validateLocation(location)) locations.add(location);
		return locations;
	}
	
	private boolean validateLocation(Location location){
		return location != null && !location.isEventCheck();
	}

	private boolean shouldCheckArea(Area area){
		return restSessionManager.isUserOnline(area.getTarget().getLogin()) &&
			   area.isActive();
	}
	
	private boolean shouldChangeMessageMail(Area area){
		return area.getAreaMessageMail().getAreaMailMessageMode() == AreaMailMessageMode.ACCEPT;
	}
}
