package com.pw.localizer.service.area;

import com.pw.localizer.model.entity.AreaPoint;
import org.primefaces.model.map.LatLng;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by wereckip on 29.09.2016.
 */

@Stateless
public class AreaPointServiceImpl implements AreaPointService {

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AreaPoint> convertToAreaPoint(List<LatLng> latLngs) {
        return IntStream.range(0,latLngs.size())
                .mapToObj(i -> {
                    LatLng latLng = latLngs.get(i);
                    return new AreaPoint(i,latLng.getLat(),latLng.getLng());
                        })
                .collect(Collectors.toList());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<LatLng> convertToLatLng(List<AreaPoint> areaPoints) {
        return areaPoints.stream()
                .map(ap -> new LatLng(ap.getLat(),ap.getLng()))
                .collect(Collectors.toList());
    }
}
