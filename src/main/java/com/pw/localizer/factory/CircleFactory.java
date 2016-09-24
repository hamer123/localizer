package com.pw.localizer.factory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.pw.localizer.identyfikator.OverlayUUIDFactory;
import com.pw.localizer.jsf.utilitis.PropertiesReader;
import com.pw.localizer.model.enums.Provider;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.LatLng;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.enums.LocalizerService;
import com.pw.localizer.model.enums.OverlayType;
import org.primefaces.push.annotation.Singleton;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;

@Singleton
@Startup
public class CircleFactory implements Serializable{
	private static CircleFactory circleFactory;
	private String GPS_CIRCLE_COLOR;
	private String NETWORK_NASZ_CIRCLE_COLOR;
	private String NETWORK_OBCY_CIRCLE_COLOR;
	private String GPS_CIRCLE_STROKE_COLOR;
	private String NETWORK_NASZ_CIRCLE_STROKE_COLOR;
	private String NETWORK_OBCY_CIRCLE_STROKE_COLOR;
	private double CIRCLE_STROKE_OPACITY;
	private double CIRCLE_FILL_OPACITY;

	@PostConstruct
	private void init(){
		PropertiesReader propertiesReader = new PropertiesReader("localizer");
		findProperties(propertiesReader);
	}

	private CircleFactory(){}

	public List<Circle> createCircle(List<Location>locationList){
		List<Circle>circleList = new ArrayList<Circle>();
		
		for(Location location : locationList){
			circleList.add(createCircleInstance(location));
		}
		
		return circleList;
	}
	
	public Circle createCircle(Location location){
		return createCircleInstance(location);
	}
	
	private Circle createCircleInstance(Location location){
		Circle circle = new Circle(new LatLng(location.getLatitude(), location.getLongitude()), 100);
		circle.setData(location);
		circle.setRadius(getRadius(location));
		circle.setFillColor(chooseColor(location));
		circle.setFillOpacity(CIRCLE_FILL_OPACITY);
		circle.setStrokeColor(chooseStrokeColor(location));
		circle.setStrokeOpacity(CIRCLE_STROKE_OPACITY);
		String uuid = OverlayUUIDFactory.builder(location, OverlayType.CIRCLE).uuid();
		circle.setId(uuid);
		return circle;
	}
	
	private double getRadius(Location location){
		return location.getAccuracy() * 100;
	}
	
	private String chooseColor(Location location){
		Provider type = location.getProviderType();
		
		if(type == Provider.GPS)
			return GPS_CIRCLE_COLOR;
		else if(type == Provider.NETWORK)
			return chooseColorNetwork(location);
		else
			throw new IllegalStateException("[CircleFactory] Nie znaleziono koloru dla providera " + type);
	}
	
	private String chooseColorNetwork(Location location){
		LocationNetwork locationNetwork = (LocationNetwork)location;
		
		if(locationNetwork.getLocalizerService() == LocalizerService.NASZ)
			return NETWORK_NASZ_CIRCLE_COLOR;
		else if(locationNetwork.getLocalizerService() == LocalizerService.OBCY)
			return NETWORK_OBCY_CIRCLE_COLOR;
		else
			throw new IllegalStateException("[CircleFactory] Nie znaleziono koloru dla Network Localization Services " + locationNetwork.getLocalizerService());
	}
	
	private String chooseStrokeColor(Location location){
		Provider type = location.getProviderType();
		
		if(type == Provider.GPS){
			return GPS_CIRCLE_STROKE_COLOR;
		} else if(type == Provider.NETWORK){
			return chooseStrokeColorNetwork(location);
		} else {
			throw new IllegalStateException("[CircleFactory] Nie znaleziono stroke koloru dla providera " + type);
		}
	}
	
	private String chooseStrokeColorNetwork(Location location){
		LocationNetwork locationNetwork = (LocationNetwork)location;
		
		if(locationNetwork.getLocalizerService() == LocalizerService.NASZ)
			return NETWORK_NASZ_CIRCLE_STROKE_COLOR;
		else if(locationNetwork.getLocalizerService() == LocalizerService.OBCY)
			return NETWORK_OBCY_CIRCLE_STROKE_COLOR;
		else
			throw new IllegalStateException("[CircleFactory] Nie znaleziono stroke koloru dla Network Localization Services " + locationNetwork.getLocalizerService());
	}
	
	private void findCircleColor(PropertiesReader propertiesReader){
		GPS_CIRCLE_COLOR = propertiesReader.findPropertyByName("GPS_CIRCLE_COLOR");
		NETWORK_NASZ_CIRCLE_COLOR = propertiesReader.findPropertyByName("NETWORK_NASZ_CIRCLE_COLOR");
		NETWORK_OBCY_CIRCLE_COLOR = propertiesReader.findPropertyByName("NETWORK_OBCY_CIRCLE_COLOR");
	}
	
	private void findCircleStrokeColor(PropertiesReader propertiesReader){
		GPS_CIRCLE_STROKE_COLOR = propertiesReader.findPropertyByName("GPS_CIRCLE_STROKE_COLOR");
		NETWORK_NASZ_CIRCLE_STROKE_COLOR = propertiesReader.findPropertyByName("NETWORK_NASZ_CIRCLE_STROKE_COLOR");
		NETWORK_OBCY_CIRCLE_STROKE_COLOR = propertiesReader.findPropertyByName("NETWORK_OBCY_CIRCLE_STROKE_COLOR");
	}
	
	private void findCircleStrokeOpacity(PropertiesReader propertiesReader){
		CIRCLE_STROKE_OPACITY = Double.parseDouble( propertiesReader.findPropertyByName("CIRCLE_STROKE_OPACITY") );
		CIRCLE_FILL_OPACITY = Double.parseDouble( propertiesReader.findPropertyByName("CIRCLE_FILL_OPACITY") );
	}
	
	private void findProperties(PropertiesReader propertiesReader){
		findCircleColor(propertiesReader);
		findCircleStrokeColor(propertiesReader);
		findCircleStrokeOpacity(propertiesReader);
	}
}

