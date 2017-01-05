package com.pw.localizer.service.location;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.qualifier.GPS;
import com.pw.localizer.repository.location.LocationGPSRepository;
import com.pw.localizer.repository.user.UserRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;

@GPS
@Stateless
public class LocationGPSServiceImp implements LocationService{
    @Inject
    private UserRepository userRepository;
    @Inject
    private LocationGPSRepository locationGPSRepository;

    public Location create(Location location) {
        User user = userRepository.findById(location.getUser().getId());
		location.setId(null);
		location = locationGPSRepository.create((LocationGPS)location);
		user.setLastLocationGPS((LocationGPS)location);
		return user.getLastLocationGPS();
    }
}
