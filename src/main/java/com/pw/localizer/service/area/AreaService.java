package com.pw.localizer.service.area;

import com.pw.localizer.model.entity.Area;

import javax.ejb.Local;

/**
 * Created by Patryk on 2016-09-15.
 */

@Local
public interface AreaService {
    void updateActive(long areaId, boolean status);
    Area update(Area area);
    void remove(long areaId);
    Area create(Area area);
}
