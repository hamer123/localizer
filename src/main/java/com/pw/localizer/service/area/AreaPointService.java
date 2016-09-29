package com.pw.localizer.service.area;

import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.AreaPoint;
import org.primefaces.model.map.LatLng;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by wereckip on 29.09.2016.
 */

@Local
public interface AreaPointService {

    List<AreaPoint> convertToAreaPoint(List<LatLng>latLngs);
    List<LatLng> convertToLatLng(List<AreaPoint>areaPoints);
}
