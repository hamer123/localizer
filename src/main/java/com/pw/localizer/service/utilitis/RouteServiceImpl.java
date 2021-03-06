package com.pw.localizer.service.utilitis;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polyline;

public class RouteServiceImpl implements RouteService, Serializable{

	private static final double EARTH_DARIUS_KM = 6378.137;

	@Override
	public double calculateLengthMeters(Polyline polyline) {
	    List<LatLng>points = polyline.getPaths();
	    double length = 0;
	    for(int i = 0; i + 1 <points.size(); i++)
	    	length += calculateLengthMeters(points.get(i), points.get(i + 1));
		return length;
	}
	
	private double calculateLengthMeters(LatLng point, LatLng point2){
	    double dLat = (point2.getLat() - point.getLat()) * Math.PI / 180;
	    double dLon = (point2.getLng() - point.getLng()) * Math.PI / 180;
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(point.getLat() * Math.PI / 180) * Math.cos(point2.getLat() * Math.PI / 180) *
	               Math.sin(dLon/2) * Math.sin(dLon/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double d = EARTH_DARIUS_KM * c;
	    return d * 1000;  // meters
	}

}
