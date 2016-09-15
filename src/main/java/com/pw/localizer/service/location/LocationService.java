package com.pw.localizer.service.location;

import javax.ejb.Local;

import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;

@Local
public interface LocationService {

	void createLocationNetworkUpdateUserCurrentLocationNetwork(LocationNetwork locationNetwork, long id);
	void createLocationGPSUpdateUserCurrentLocationGPS(LocationGPS locationGPS, long id);
}
