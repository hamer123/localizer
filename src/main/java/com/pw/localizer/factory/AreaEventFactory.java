package com.pw.localizer.factory;

import java.util.UUID;
import com.pw.localizer.model.entity.*;
import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.enums.AreaMailMessageMode;
import com.pw.localizer.utilitis.AreaEventMessageBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AreaEventFactory {

	public AreaEvent create(Area area, Location location) {
		AreaEvent areaEvent = createInstance(location);
		areaEvent.setArea(area);
		areaEvent.setDate(location.getDate());
		areaEvent.setSendMail(shouldSendMailMessage(area.getAreaMessageMail()));
		areaEvent.setMessage(AreaEventMessageBuilder.create(area, location));
		areaEvent.setAccessToken(UUID.randomUUID().toString());
		return areaEvent;
	}
	
	private AreaEvent createInstance(Location location) {
		if(location instanceof LocationNetwork) {
			return new AreaEventNetwork((LocationNetwork) location);
		} else {
			return new AreaEventGPS((LocationGPS) location);
		}
	}
	
	private boolean shouldSendMailMessage(AreaMessageMail areaMessageMail) {
		if(areaMessageMail.isActive()) {
			if( areaMessageMail.getAreaMailMessageMode() == AreaMailMessageMode.ACCEPT && areaMessageMail.isAccept()) {
				return true;
			} else if( areaMessageMail.getAreaMailMessageMode() == AreaMailMessageMode.EVER ) {
				return true;
			}
		}
		return false;
	}
}


