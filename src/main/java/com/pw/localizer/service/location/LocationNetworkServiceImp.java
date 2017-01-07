package com.pw.localizer.service.location;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.entity.WifiInfo;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.qualifier.Network;
import com.pw.localizer.repository.location.CellInfoMobileRepository;
import com.pw.localizer.repository.location.LocationNetworkRepository;
import com.pw.localizer.repository.location.WifiInfoRepository;
import com.pw.localizer.repository.user.UserRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Network
@Stateless
public class LocationNetworkServiceImp implements LocationService<LocationNetwork>{
    @Inject
    private UserRepository userRepository;
    @Inject
    private LocationNetworkRepository locationNetworkRepository;
	@Inject
	private WifiInfoRepository wifiInfoRepository;
	@Inject
	private CellInfoMobileRepository cellInfoMobileRepository;

	public LocationNetwork create(LocationNetwork location) {
		User user = userRepository.findById(location.getUser().getId());
		location.setId(null);
		location = locationNetworkRepository.create(location);
		updateUserCurrentLocationNetwork(location, user);
		return location;
    }

	public LocationNetwork fetchRelations(LocationNetwork location) {
		location.setWifiInfo(wifiInfoRepository.findByLocationId(location.getId()));
		location.setCellInfoMobile(cellInfoMobileRepository.findByLocationId(location.getId()));
		return location;
	}

	private void updateUserCurrentLocationNetwork(LocationNetwork locationNetwork, User user){
		if(locationNetwork.getLocalizationService() == LocalizationService.NASZ)
			user.setLastLocationNetworkNaszaUsluga(locationNetwork);
		else if(locationNetwork.getLocalizationService() == LocalizationService.OBCY)
			user.setLastLocationNetworObcaUsluga(locationNetwork);
	}
}
