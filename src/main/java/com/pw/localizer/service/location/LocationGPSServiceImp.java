package com.pw.localizer.service.location;

import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.qualifier.GPS;
import com.pw.localizer.repository.location.LocationGPSRepository;
import com.pw.localizer.repository.user.UserRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;

@GPS
@Stateless
public class LocationGPSServiceImp implements LocationService<LocationGPS>{
    @Inject
    private UserRepository userRepository;
    @Inject
    private LocationGPSRepository locationGPSRepository;

    public LocationGPS create(LocationGPS location) {
        User user = userRepository.findById(location.getUser().getId());
		location.setId(null);
		location = locationGPSRepository.create(location);
		user.setLastLocationGPS(location);
		return user.getLastLocationGPS();
    }

    public LocationGPS fetchRelations(LocationGPS location) {
        return location;
    }
}
