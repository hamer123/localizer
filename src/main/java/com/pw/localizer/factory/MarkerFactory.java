package com.pw.localizer.factory;

import java.io.Serializable;

import com.pw.localizer.identyfikator.OverlayUUIDFactory;
import com.pw.localizer.singleton.LocalizerProperties;
import com.pw.localizer.model.enums.LocalizationService;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.enums.OverlayType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MarkerFactory implements Serializable {
	@Inject
	private LocalizerProperties localizerProperties;

	public Marker createStartRouteMarker(Location location) {
		Marker marker = createMarkerInstance(location);
		marker.setIcon(localizerProperties.Marker().START_ROUTE);
		return marker;
	}

	public Marker createEndRouteMarker(Location location) {
		Marker marker = createMarkerInstance(location);
		marker.setIcon(localizerProperties.Marker().END_ROUTE);
		return marker;
	}

	public Marker createMarker(Location location){
		return createMarkerInstance(location);
	}
	
	Marker createMarkerInstance(Location location){
		Marker marker = new Marker(new LatLng(location.getLatitude(), location.getLongitude()));
		marker.setIcon(chooseIcon(location));
		marker.setData(location);
		marker.setDraggable(false);
		marker.setClickable(true);
		marker.setTitle(createTitle(location));
		String uuid = OverlayUUIDFactory.builder(location, OverlayType.MARKER).uuid();
		marker.setId(uuid);
		return marker;
	}
	
	String createTitle(Location location){
		return location.getProviderType()
				+ " "
				+ location.getUser().getLogin()
				+ " "
				+ location .getDate();
	}

	String chooseIcon(Location location){
		switch(location.getProviderType()){
		case GPS:
			return localizerProperties.Marker().GPS;
		case NETWORK:
			LocationNetwork locationNetwork = (LocationNetwork)location;
			if(locationNetwork.getLocalizationService() == LocalizationService.NASZ)
				return localizerProperties.Marker().NETWORK_NASZ;
			else if(locationNetwork.getLocalizationService() == LocalizationService.OBCY)
				return localizerProperties.Marker().NETWORK_OBCY;
		default:
			throw new IllegalArgumentException("Nie ma dla takiego providera icony");
		}
	}
}

