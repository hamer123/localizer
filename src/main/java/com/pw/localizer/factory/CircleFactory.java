package com.pw.localizer.factory;

import java.io.Serializable;
import com.pw.localizer.identyfikator.OverlayUUIDFactory;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.singleton.LocalizerProperties;
import com.pw.localizer.model.enums.Provider;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.LatLng;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.enums.OverlayType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CircleFactory implements Serializable{
	@Inject
	private LocalizerProperties localizerProperties;

	public Circle createCircle(Location location){
		return createCircleInstance(location);
	}
	
	private Circle createCircleInstance(Location location){
		Circle circle = new Circle(new LatLng(location.getLatitude(), location.getLongitude()), 100);
		circle.setData(location);
		circle.setRadius(getRadius(location));
		circle.setFillColor(chooseColor(location));
		circle.setFillOpacity(localizerProperties.Circle().FILL_OPACITY);
		circle.setStrokeColor(chooseStrokeColor(location));
		circle.setStrokeOpacity(localizerProperties.Circle().STROKE_OPACITY);
		String uuid = OverlayUUIDFactory.builder(location, OverlayType.CIRCLE).uuid();
		circle.setId(uuid);
		return circle;
	}
	
	private double getRadius(Location location){
		return location.getAccuracy() * 100;
	}
	
	private String chooseColor(Location location){
		Provider type = location.getProviderType();
		if(type == Provider.GPS) {
			return localizerProperties.Circle().GPS_COLOR;
		} else if(type == Provider.NETWORK) {
			return chooseColorNetwork(location);
		} else {
			throw new IllegalStateException("Nie znaleziono koloru dla providera " + type);
		}
	}
	
	private String chooseColorNetwork(Location location){
		LocationNetwork locationNetwork = (LocationNetwork)location;
		if(locationNetwork.getLocalizationService() == LocalizationService.NASZ) {
			return localizerProperties.Circle().NETWORK_NASZ_COLOR;
		} else if(locationNetwork.getLocalizationService() == LocalizationService.OBCY) {
			return localizerProperties.Circle().NETWORK_OBCY_COLOR;
		} else {
			throw new IllegalStateException("Nie znaleziono koloru dla Network Localization Services " + locationNetwork.getLocalizationService());
		}
	}
	
	private String chooseStrokeColor(Location location){
		Provider type = location.getProviderType();
		if(type == Provider.GPS){
			return localizerProperties.Circle().GPS_STROKE_COLOR;
		} else if(type == Provider.NETWORK){
			return chooseStrokeColorNetwork(location);
		} else {
			throw new IllegalStateException("Nie znaleziono stroke koloru dla providera " + type);
		}
	}
	
	private String chooseStrokeColorNetwork(Location location){
		LocationNetwork locationNetwork = (LocationNetwork)location;
		if(locationNetwork.getLocalizationService() == LocalizationService.NASZ) {
			return localizerProperties.Circle().NETWORK_NASZ_STROKE_COLOR;
		} else if(locationNetwork.getLocalizationService() == LocalizationService.OBCY) {
			return localizerProperties.Circle().NETWORK_OBCY_STROKE_COLOR;
		} else {
			throw new IllegalStateException("Nie znaleziono stroke koloru dla Network Localization Services " + locationNetwork.getLocalizationService());
		}
	}
}

