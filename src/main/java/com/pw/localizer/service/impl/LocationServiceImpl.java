package com.pw.localizer.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.enums.LocalizationServices;
import com.pw.localizer.repository.LocationGPSRepository;
import com.pw.localizer.repository.LocationNetworkRepository;
import com.pw.localizer.repository.UserRepository;
import com.pw.localizer.service.LocationService;


/** NA przyszlosc mozna zmienic argument na Usera i zrobic update dla usera lokacji przez zwykle query*/
@Stateless
public class LocationServiceImpl implements LocationService{
	@Inject
	private UserRepository userRepository;
	@Inject
	private LocationGPSRepository locationGPSRepository;
	@Inject
	private LocationNetworkRepository locationNetworkRepository;

	@Override
	public void createLocationNetworkUpdateUserCurrentLocationNetwork(LocationNetwork locationNetwork, long userId) {
		User user = userRepository.findById(userId);
		locationNetwork.setUser(user);
		locationNetworkRepository.create(locationNetwork);
		updateUserCurrentLocationNetwork(locationNetwork, user);
	}

	@Override
	public void createLocationGPSUpdateUserCurrentLocationGPS(LocationGPS locationGPS, long userId) {
		User user = userRepository.findById(userId);
		locationGPS.setUser(user);
		locationGPSRepository.create(locationGPS);
		user.setLastLocationGPS(locationGPS);
	}
	
	private void updateUserCurrentLocationNetwork(LocationNetwork locationNetwork, User user){
		if(locationNetwork.getLocalizationServices() == LocalizationServices.NASZ)
			user.setLastLocationNetworkNaszaUsluga(locationNetwork);
		else if(locationNetwork.getLocalizationServices() == LocalizationServices.OBCY)
			user.setLastLocationNetworObcaUsluga(locationNetwork);
	}

}
