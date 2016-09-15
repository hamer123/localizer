package com.pw.localizer.service.utilitis;

import java.util.List;
import java.util.Set;

import org.primefaces.model.map.Polygon;

import com.pw.localizer.model.google.map.GoogleMapComponentVisible;
import com.pw.localizer.model.google.component.GoogleLocation;
import com.pw.localizer.model.google.component.Route;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.User;

public interface GoogleMapUserComponentService {
	List<GoogleLocation> lastLocations(User user);
	List<GoogleLocation> lastLocations(Set<User> users);
	List<GoogleLocation> lastLocations(User user, GoogleMapComponentVisible visible);
	List<GoogleLocation> lastLocations(Set<User> users, GoogleMapComponentVisible visible);
	List<Polygon> polygons(User user);
	Polygon polygon(Area area);
	Polygon polygon(Area area, GoogleMapComponentVisible visible);
	GoogleLocation position(Location location);
	Route route(List<Location>locations);
	Route routeNoMarkers(List<Location>locations);
}
