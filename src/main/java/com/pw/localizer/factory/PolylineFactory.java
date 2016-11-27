package com.pw.localizer.factory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.pw.localizer.identyfikator.OverlayUUIDBuilderLocation;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.singleton.LocalizerProperties;
import com.pw.localizer.model.enums.Provider;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polyline;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.enums.OverlayType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PolylineFactory implements Serializable {

	@Inject
	private LocalizerProperties localizerProperties;

	public Polyline create(List<Location>locations){
		Polyline polyline = new Polyline();
		polyline.setId(id(locations.get(0)));
		polyline.setData(locations);
		polyline.setPaths(path(locations));
		polyline.setStrokeColor(color(polyline, locations.get(0)));
		polyline.setStrokeOpacity(localizerProperties.Polyline().STROKE_OPACITY);
		polyline.setStrokeWeight(localizerProperties.Polyline().STROKE_WEIGHT);
		return polyline;
	}

	public Polyline create(Location location){
		Polyline polyline = new Polyline();
		List<Location>locations = new ArrayList<>();
		locations.add(location);
		polyline.setId(id(location));
		polyline.setPaths(path(locations));
		polyline.setStrokeColor(color(polyline, location));
		polyline.setStrokeOpacity(localizerProperties.Polyline().STROKE_OPACITY);
		polyline.setStrokeWeight(localizerProperties.Polyline().STROKE_WEIGHT);
		return polyline;
	}

	public Polyline create(Location location, Object data){
		Polyline polyline = create(location);
		polyline.setData(data);
		return polyline;
	}


	private String color(Provider provider, LocalizationService localizationService){
		if(provider == Provider.GPS) {
			return localizerProperties.Polyline().GPS_COLOR;
		} else if(provider == Provider.NETWORK) {
			if(localizationService.equals(LocalizationService.NASZ)) {
				return localizerProperties.Polyline().NETWORK_NASZ_COLOR;
			} else {
				return localizerProperties.Polyline().NETWORK_OBCY_COLOR;
			}
		} else {
			throw new RuntimeException("Nie oblusgiwany Provider " + provider);
		}
	}

	private String id(Location location){
		return new OverlayUUIDBuilderLocation(location, OverlayType.POLYLINE).uuid();
	}
	
	private List<LatLng> path(List<Location>locations){
		List<LatLng>paths = new ArrayList<LatLng>();
		locations.forEach((location) -> paths.add(new LatLng(location.getLatitude(), location.getLongitude())));
		return paths;
	}
	
	private String color(Polyline polyline, Location location){
		if(location instanceof LocationGPS) {
			return localizerProperties.Polyline().GPS_COLOR;
		} else {
			LocationNetwork locationNetwork = (LocationNetwork)location;
			if(locationNetwork.getLocalizationService() == LocalizationService.NASZ) {
				return localizerProperties.Polyline().NETWORK_NASZ_COLOR;
			} else {
				return localizerProperties.Polyline().NETWORK_OBCY_COLOR;
			}
		}
	}
}
