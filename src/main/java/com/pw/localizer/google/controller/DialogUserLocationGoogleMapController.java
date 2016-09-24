package com.pw.localizer.google.controller;

import javax.enterprise.context.Dependent;

import com.pw.localizer.model.entity.LocationNetwork;

import com.pw.localizer.model.google.GoogleMapModel;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.enums.LocalizerService;
import com.pw.localizer.qualifier.DialogGMap;

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
		String center = GoogleMapModel.center(selectedLocation);
		setCenter(center);
	}

	public Location getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(Location selectedLocation) {
		this.selectedLocation = selectedLocation;
	}

	public LocalizerService getLocalizationServices(Location location){
		if(location instanceof LocationNetwork)
			return ( (LocationNetwork)location ).getLocalizerService();
		return null;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
}
