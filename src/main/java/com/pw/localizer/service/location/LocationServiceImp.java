package com.pw.localizer.service.location;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.repository.location.LocationGPSRepository;
import com.pw.localizer.repository.location.LocationNetworkRepository;
import com.pw.localizer.repository.user.UserRepository;

@Stateless
public class LocationServiceImp implements LocationService{
	@Inject
	private UserRepository userRepository;
	@Inject
	private LocationGPSRepository locationGPSRepository;
	@Inject
	private LocationNetworkRepository locationNetworkRepository;

	@Override
	public LocationNetwork createLocationNetwork(LocationNetwork locationNetwork) {
		User user = userRepository.findById(locationNetwork.getUser().getId());
		locationNetwork.setId(null);
		locationNetwork = locationNetworkRepository.create(locationNetwork);
		updateUserCurrentLocationNetwork(locationNetwork, user);
		return locationNetwork;
	}

	@Override
	public LocationGPS createLocationGPS(LocationGPS locationGPS) {
		User user = userRepository.findById(locationGPS.getUser().getId());
		locationGPS.setId(null);
		locationGPS = locationGPSRepository.create(locationGPS);
		user.setLastLocationGPS(locationGPS);
		return user.getLastLocationGPS();
	}
	
	private void updateUserCurrentLocationNetwork(LocationNetwork locationNetwork, User user){
		if(locationNetwork.getLocalizationService() == LocalizationService.NASZ)
			user.setLastLocationNetworkNaszaUsluga(locationNetwork);
		else if(locationNetwork.getLocalizationService() == LocalizationService.OBCY)
			user.setLastLocationNetworObcaUsluga(locationNetwork);
	}

}
