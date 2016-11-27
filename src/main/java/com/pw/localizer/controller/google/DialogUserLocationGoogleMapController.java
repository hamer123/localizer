package com.pw.localizer.controller.google;

import javax.enterprise.context.Dependent;

import com.pw.localizer.model.entity.LocationNetwork;

import com.pw.localizer.model.google.GoogleMap;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.enums.LocalizationService;

import java.util.ArrayList;
import java.util.List;

@Dependent
@DialogGMap
public class DialogUserLocationGoogleMapController extends GoogleMapController {

	private List<Location>locations = new ArrayList<>();

	private Location selectedLocation;

	private String userLogin;

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public void onShowLcation(){
		String center = GoogleMap.center(selectedLocation);
		setCenter(center);
	}

	public Location getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(Location selectedLocation) {
		this.selectedLocation = selectedLocation;
	}

	public LocalizationService getLocalizationServices(Location location){
		if(location instanceof LocationNetwork)
			return ( (LocationNetwork)location ).getLocalizationService();
		return null;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
}
