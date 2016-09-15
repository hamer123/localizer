package com.pw.localizer.factory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.pw.localizer.jsf.utilitis.OverlayIdentyfikator;
import com.pw.localizer.jsf.utilitis.PropertiesReader;
import com.pw.localizer.model.enums.Providers;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polyline;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.enums.LocalizationServices;
import com.pw.localizer.model.enums.Overlays;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Stateless;

@Stateless
@Startup
public class PolylineFactory implements Serializable {
	private String GPS_POLYLINE_COLOR;
	private String NETWORK_NASZ_POLYLINE_COLOR;
	private String NETWORK_OBCY_POLYLINE_COLOR;
	private double POLYLINE_STROKE_OPACITY;
	private int POLYLINE_STROKE_WEIGHT;

	@PostConstruct
	public void init(){
		PropertiesReader propertiesReader = new PropertiesReader("localizer");
		findProperties(propertiesReader);
	}

	public Polyline create(List<Location>locations){
		Polyline polyline = new Polyline();
		polyline.setId( id(locations.get(0)) );
		polyline.setData ( locations );
		polyline.setPaths( path(locations) );
		polyline.setStrokeColor( color(polyline, locations.get(0)) );
		polyline.setStrokeOpacity( POLYLINE_STROKE_OPACITY );
		polyline.setStrokeWeight( POLYLINE_STROKE_WEIGHT );
		return polyline;
	}

	public Polyline create(Location location){
		Polyline polyline = new Polyline();
		List<Location>locations = new ArrayList<>();
		locations.add(location);
		polyline.setId(id(location));
		polyline.setPaths(path(locations));
		polyline.setStrokeColor( color(polyline, location) );
		polyline.setStrokeOpacity( POLYLINE_STROKE_OPACITY );
		polyline.setStrokeWeight( POLYLINE_STROKE_WEIGHT );
		return polyline;
	}

	public Polyline create(Location location, Object data){
		Polyline polyline = create(location);
		polyline.setData(data);
		return polyline;
	}


	private String color(Providers providers, LocalizationServices localizationServices){
		if(providers == Providers.GPS)
			return GPS_POLYLINE_COLOR;
		else if(providers == Providers.NETWORK){
			if(localizationServices.equals(LocalizationServices.NASZ))
				return NETWORK_NASZ_POLYLINE_COLOR;
			else
				return NETWORK_OBCY_POLYLINE_COLOR;
		} else {
			throw new RuntimeException("Nie oblusgiwany Provider " + providers);
		}
	}

	private String id(Location location){
		return new OverlayIdentyfikator(location, Overlays.POLYLINE).createIdentyfikator();
	}
	
	private List<LatLng> path(List<Location>locations){
		List<LatLng>paths = new ArrayList<LatLng>();
		
		for(Location location : locations)
			paths.add(new LatLng(location.getLatitude(), location.getLongitude()));
		
		return paths;
	}
	
	private String color(Polyline polyline, Location location){
		if(location instanceof LocationGPS){
			return GPS_POLYLINE_COLOR;
		} else {
			LocationNetwork locationNetwork = (LocationNetwork)location;
			
			if(locationNetwork.getLocalizationServices() == LocalizationServices.NASZ)
				return NETWORK_NASZ_POLYLINE_COLOR;
			else
				return NETWORK_OBCY_POLYLINE_COLOR;
		}
	}
	
	private void findProperties(PropertiesReader propertiesReader){
		GPS_POLYLINE_COLOR = propertiesReader.findPropertyByName("GPS_POLYLINE_COLOR");
		NETWORK_NASZ_POLYLINE_COLOR = propertiesReader.findPropertyByName("NETWORK_NASZ_POLYLINE_COLOR");
		NETWORK_OBCY_POLYLINE_COLOR = propertiesReader.findPropertyByName("NETWORK_OBCY_POLYLINE_COLOR");
		POLYLINE_STROKE_OPACITY = Double.valueOf( propertiesReader.findPropertyByName("POLYLINE_STROKE_OPACITY") );
		POLYLINE_STROKE_WEIGHT = Integer.valueOf( propertiesReader.findPropertyByName("POLYLINE_STROKE_WEIGHT") );
	}
}
