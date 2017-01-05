package com.pw.localizer.job;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;
import com.pw.localizer.model.entity.AreaEvent;
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
	public void work() {
		List<Area> activeAreas = areaRepository.findByActive(true);
		logger.info("There are " + activeAreas.size() + " actives area to check!");
		for(Area area : activeAreas) {
			if(shouldCheckArea(area)) {
				User target = area.getTarget();
				List<Location>userLastLocations = userLastLocations(target);
				for(Location location : userLastLocations) {
					if(shouldCreateAreaEvent(area, location)) {
						createAreaEvent(area, location);
						if(shouldChangeMessageMail(area)) {
							area.getAreaMessageMail().setAccept(false);
						}
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
	AreaEvent createAreaEvent(Area area, Location location){
		AreaEvent areaEvent = areaEventFactory.create(area, location);
		location.setEventCheck(true);
		return areaEventService.create(areaEvent);
	}

	List<Location> userLastLocations(User user){
		List<Location>locations = new ArrayList<>();
		Location location = user.getLastLocationGPS();
		if(validateLocation(location)) locations.add(location);
		location = user.getLastLocationNetworkNaszaUsluga();
		if(validateLocation(location)) locations.add(location);
		location = user.getLastLocationNetworObcaUsluga();
		if(validateLocation(location)) locations.add(location);
		return locations;
	}
	
	boolean validateLocation(Location location){
		return  location != null &&
				!location.isEventCheck();
	}
	
	boolean shouldChangeMessageMail(Area area){
		return area.getAreaMessageMail().getAreaMailMessageMode() == AreaMailMessageMode.ACCEPT;
	}

	boolean shouldCreateAreaEvent(Area area, Location location) {
		if(area.getAreaFollowType() == AreaFollow.INSIDE) {
			return !area.contains(location);
		} else {
			return area.contains(location);
		}
	}

	boolean shouldCheckArea(Area area) {
		return  restSessionManager.isUserOnline(area.getTarget().getLogin());
	}
}
