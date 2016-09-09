package com.pw.localizer.overlay;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.pw.localizer.identyfikator.OverlayUUIDFactory;
import com.pw.localizer.jsf.utilitis.PropertiesReader;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polygon;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.AreaPoint;

public class PolygonBuilder {
	private static PolygonBuilder polygonBuilder;
	private String POLYGON_STROKE_COLOR;
	private String POLYGON_FILL_COLOR;
	private double POLYGON_FILL_OPACITY;
	private double POLYGON_STROKE_OPACITY;

	public static PolygonBuilder getInstance(){
		if(polygonBuilder == null){
			synchronized (PolygonBuilder.class){
				if(polygonBuilder == null)
					polygonBuilder = new PolygonBuilder();
			}
		}

		return polygonBuilder;
	}

	public PolygonBuilder(){
		PropertiesReader propertiesReader = new PropertiesReader("localizer");
		findProperties(propertiesReader);
	}

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
		polygon.setFillColor(POLYGON_FILL_COLOR);
		polygon.setFillOpacity(POLYGON_FILL_OPACITY);
		polygon.setStrokeColor(POLYGON_STROKE_COLOR);
		polygon.setStrokeOpacity(POLYGON_STROKE_OPACITY);
	}

	private void setupPolygonView(Polygon polygon, Area area){
		setPolygonFillColor(polygon, area.getColor());
		polygon.setFillOpacity(POLYGON_FILL_OPACITY);
		polygon.setStrokeColor(POLYGON_STROKE_COLOR);
		polygon.setStrokeOpacity(POLYGON_STROKE_OPACITY);
	}

	private void setPolygonFillColor(Polygon polygon, String color){
		if(color.startsWith("#"))
			polygon.setFillColor(color);
		else
			polygon.setFillColor("#" + color);
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

	private List<LatLng> createPaths(Map<Integer, AreaPoint> polygonPointsMap){
		List<LatLng>latlngList = new ArrayList<>();

		List<Integer>numberList = getSortedPointNumber( polygonPointsMap.keySet() );
		for(int number : numberList){
			AreaPoint polygonPoint = polygonPointsMap.get(number);
			LatLng latLng = new LatLng(polygonPoint.getLat(), polygonPoint.getLng());
			latlngList.add(latLng);
		}

		return latlngList;
	}

	private List<Integer> getSortedPointNumber(Collection<Integer>numberCollection){
		List<Integer>pointSortedList = new ArrayList<Integer>(numberCollection);
		Collections.sort(pointSortedList);
		return pointSortedList;
	}

	private void findProperties(PropertiesReader propertiesReader){
		findFillColorAndOpacity(propertiesReader);
		findStrokeColorAndOpacity(propertiesReader);
	}

	private void findFillColorAndOpacity(PropertiesReader propertiesReader){
		POLYGON_FILL_COLOR = propertiesReader.findPropertyByName("POLYGON_FILL_COLOR");
		POLYGON_STROKE_COLOR = propertiesReader.findPropertyByName("POLYGON_STROKE_COLOR");
	}

	private void findStrokeColorAndOpacity(PropertiesReader propertiesReader){
		POLYGON_FILL_OPACITY = Double.valueOf( propertiesReader.findPropertyByName("POLYGON_FILL_OPACITY") );
		POLYGON_STROKE_OPACITY = Double.valueOf( propertiesReader.findPropertyByName("POLYGON_STROKE_OPACITY") );
	}
}
