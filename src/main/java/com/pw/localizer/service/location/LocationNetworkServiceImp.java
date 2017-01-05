package com.pw.localizer.service.location;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.qualifier.Network;
import com.pw.localizer.repository.location.LocationNetworkRepository;
import com.pw.localizer.repository.user.UserRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Network
@Stateless
public class LocationNetworkServiceImp implements LocationService{
    @Inject
    private UserRepository userRepository;
    @Inject
    private LocationNetworkRepository locationNetworkRepository;

    public Location create(Location location) {
        LocationNetwork locationNetwork = (LocationNetwork) location;
		User user = userRepository.findById(locationNetwork.getUser().getId());
		locationNetwork.setId(null);
		locationNetwork = locationNetworkRepository.create(locationNetwork);
		updateUserCurrentLocationNetwork(locationNetwork, user);
		return locationNetwork;
    }

    	private void updateUserCurrentLocationNetwork(LocationNetwork locationNetwork, User user){
		if(locationNetwork.getLocalizationService() == LocalizationService.NASZ)
			user.setLastLocationNetworkNaszaUsluga(locationNetwork);
		else if(locationNetwork.getLocalizationService() == LocalizationService.OBCY)
			user.setLastLocationNetworObcaUsluga(locationNetwork);
	}
}
