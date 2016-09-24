package com.pw.localizer.service.area;

import com.pw.localizer.model.entity.Area;
import com.pw.localizer.repository.area.AreaRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by Patryk on 2016-09-23.
 */

@Stateless
public class AreaServiceImpl implements AreaService{
    @Inject
    private AreaRepository areaRepository;

    @Override
    public void updateActive(long areaId, boolean status) {
        areaRepository.updateSetActiveById(status,areaId);
    }

    @Override
    public Area update(Area area) {
        return areaRepository.save(area);
    }

    @Override
    public void remove(long areaId) {
        areaRepository.delete(areaRepository.findById(areaId));
    }

    @Override
    public Area create(Area area) {
        return areaRepository.create(area);
    }
}
