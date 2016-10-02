package com.pw.localizer.service.location;

import javax.ejb.Local;

import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;

@Local
public interface LocationService {

	/** Create new network location that becomes his last location */
	LocationNetwork createLocationNetwork(LocationNetwork locationNetwork, long userId);

	/** Create new gps location that becomes his last location */
	LocationGPS createLocationGPS(LocationGPS locationGPS);
}
