package com.pw.localizer.service.location;

import javax.ejb.Local;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;

@Local
public interface LocationService<T extends Location> {

    /** Create Location */
    T create(T location);

    /** Fetch others relation */
    T fetchRelations(T location);
}
