package com.pw.localizer.factory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.pw.localizer.identyfikator.OverlayUUIDFactory;
import com.pw.localizer.singleton.LocalizerProperties;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polygon;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.AreaPoint;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PolygonFactory implements Serializable {

	@Inject
	private LocalizerProperties localizerProperties;

	public Polygon empty(){
		Polygon polygon = new Polygon();
		setupPolygonView(polygon);
		return polygon;
	}

	public Polygon create(Area area, Object data){
		Polygon polygon = createPolygonInstance(area);
		polygon.setData(data);
		return polygon;
	}

	public Polygon create(Area area){
		return createPolygonInstance(area);
	}

	public List<Polygon> create(List<Area>areas){
		List<Polygon>polygonList = new ArrayList<Polygon>();
		for(Area area : areas)
			polygonList.add(createPolygonInstance(area));
		return polygonList;
	}

	private void setupPolygonView(Polygon polygon){
		polygon.setFillColor(localizerProperties.Polygon().FILL_COLOR);
		polygon.setFillOpacity(localizerProperties.Polygon().FILL_OPACITY);
		polygon.setStrokeColor(localizerProperties.Polygon().STROKE_COLOR);
		polygon.setStrokeOpacity(localizerProperties.Polygon().STROKE_OPACITY);
	}

	private void setupPolygonView(Polygon polygon, Area area){
		setPolygonFillColor(polygon, area.getColor());
		polygon.setFillOpacity(localizerProperties.Polygon().FILL_OPACITY);
		polygon.setStrokeColor(localizerProperties.Polygon().STROKE_COLOR);
		polygon.setStrokeOpacity(localizerProperties.Polygon().STROKE_OPACITY);
	}

	private void setPolygonFillColor(Polygon polygon, String color){
		if(color.startsWith("#")) {
			polygon.setFillColor(color);
		} else {
			polygon.setFillColor("#" + color);
		}
	}

	private Polygon createPolygonInstance(Area area){
		Polygon polygon = new Polygon();
		setupPolygonView(polygon, area);
		String uuid = OverlayUUIDFactory.builder(area).uuid();
		polygon.setId(uuid);
		List<LatLng>pathList = createPaths(area.getPoints());
		polygon.setPaths(pathList);
		return polygon;
	}

	private List<LatLng> createPaths(List<AreaPoint> areaPoints){
		return areaPoints.stream()
				.map(a -> new LatLng(a.getLat(),a.getLng()))
				.collect(Collectors.toList());
	}
}
