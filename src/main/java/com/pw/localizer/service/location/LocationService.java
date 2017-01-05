package com.pw.localizer.service.location;

import javax.ejb.Local;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;

@Local
public interface LocationService {

    /** Create Location */
    Location create(Location location);
}
