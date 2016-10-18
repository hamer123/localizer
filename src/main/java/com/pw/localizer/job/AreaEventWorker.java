package com.pw.localizer.job;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;

import com.pw.localizer.model.entity.AreaEvent;
import org.jboss.logging.Logger;

import com.pw.localizer.factory.AreaEventFactory;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.enums.AreaFollow;
import com.pw.localizer.model.enums.AreaMailMessageMode;
import com.pw.localizer.repository.area.AreaRepository;
import com.pw.localizer.singleton.RestSessionManager;

@Stateless
@LocalBean
public class AreaEventWorker {
	@Inject
	private RestSessionManager restSessionManager;
	@Inject
	private AreaRepository areaRepository;
	@Inject
	private AreaEventFactory areaEventFactory;
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
		try{
			logger.info("job has started");
			return ic.proceed();
		} finally{
			logger.info("job has ended after " + (System.currentTimeMillis() - time) + "ms");
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private AreaEvent createAreaEvent(Location location, Area area){
		AreaEvent areaEvent = areaEventFactory.create(area, location);
		location.setEventCheck(true);
		return areaEvent;
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
