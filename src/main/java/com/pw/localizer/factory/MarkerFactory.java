package com.pw.localizer.factory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.pw.localizer.identyfikator.OverlayUUIDFactory;
import com.pw.localizer.jsf.utilitis.PropertiesReader;
import com.pw.localizer.model.enums.LocalizerService;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.enums.OverlayType;
import org.primefaces.push.annotation.Singleton;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;

@Singleton
@Startup
public class MarkerFactory implements Serializable {
	public String GPS_MARKER_ICON_URL;
	public String NETWORK_MARKER_OBCA_USLUGA_ICON_URL;
	public String NETWORK_MARKER_NASZA_USLUGA_ICON_URL;
	public String START_ROUTE_ICON_URL;
	public String END_ROUTE_ICON_URL;

	@PostConstruct
	private void init(){
		PropertiesReader propertiesReader = new PropertiesReader("localizer");
		findProperties(propertiesReader);
	}
	
	private Marker createMarkerInstance(Location location){
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
	
	private static String createTitle(Location location){
		String title = location.getProviderType()
				     + " "
				     + location.getUser().getLogin()
				     + " "
				     + location .getDate();
		return title;
	}

	private String chooseIcon(Location location){
		switch(location.getProviderType()){
		case GPS:
			return GPS_MARKER_ICON_URL;
		case NETWORK:
			LocationNetwork locationNetwork = (LocationNetwork)location;
			if(locationNetwork.getLocalizerService() == LocalizerService.NASZ)
				return NETWORK_MARKER_NASZA_USLUGA_ICON_URL;
			else if(locationNetwork.getLocalizerService() == LocalizerService.OBCY)
				return NETWORK_MARKER_OBCA_USLUGA_ICON_URL;
		default:
			throw new IllegalArgumentException("Nie ma dla takiego providera icony");
		}
	}
	
	public List<Marker> createMarker(List<Location>locationList){
		List<Marker>markerList = new ArrayList<Marker>();
		
		for(Location location : locationList)
			markerList.add(createMarkerInstance(location));
		
		return markerList;
	}

	private void findMarkerIconUrl(PropertiesReader propertiesReader){
		GPS_MARKER_ICON_URL = propertiesReader.findPropertyByName("GPS_MARKER_ICON_URL");
		NETWORK_MARKER_NASZA_USLUGA_ICON_URL = propertiesReader.findPropertyByName("NETWORK_MARKER_NASZA_USLUGA_ICON_URL");
		NETWORK_MARKER_OBCA_USLUGA_ICON_URL  = propertiesReader.findPropertyByName("NETWORK_MARKER_OBCA_USLUGA_ICON_URL");
		START_ROUTE_ICON_URL = propertiesReader.findPropertyByName("START_ROUTE_ICON_URL");
		END_ROUTE_ICON_URL = propertiesReader.findPropertyByName("END_ROUTE_ICON_URL");
	}

	private void findProperties(PropertiesReader propertiesReader){
		findMarkerIconUrl(propertiesReader);
	}
	
	public Marker createMarker(Location location){
		return createMarkerInstance(location);
	}
}

