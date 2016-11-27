package com.pw.localizer.model.google;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pw.localizer.identyfikator.OverlayUUIDRaw;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;
import org.primefaces.model.map.Rectangle;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.enums.OverlayType;

public class GoogleMap implements MapModel, Serializable{

	private List<Marker> markers;
	
	private List<Polyline> polylines;
	
	private List<Polygon> polygons;
        
    private List<Circle> circles;
        
    private List<Rectangle> rectangles;

	public GoogleMap(List<Marker> markers, List<Polyline> polylines,
                     List<Polygon> polygons, List<Circle> circles,
                     List<Rectangle> rectangles) {
		this.markers = markers;
		this.polylines = polylines;
		this.polygons = polygons;
		this.circles = circles;
		this.rectangles = rectangles;
	}

	public GoogleMap() {
		markers = new ArrayList<Marker>();
		polylines = new ArrayList<Polyline>();
		polygons = new ArrayList<Polygon>();
		circles = new ArrayList<Circle>();
		rectangles = new ArrayList<Rectangle>();
	}

	public List<Marker> getMarkers() {
		return markers;
	}

	public List<Polyline> getPolylines() {
		return polylines;
	}

	public List<Polygon> getPolygons() {
		return polygons;
	}
	
    public List<Circle> getCircles() {
		return circles;
	}
        
    public List<Rectangle> getRectangles() {
		return rectangles;
	}

	public void addOverlay(Overlay overlay) {
		if(overlay instanceof Marker) {
			markers.add((Marker) overlay);
		}
		else if(overlay instanceof Polyline) {
			polylines.add((Polyline) overlay);
		}
		else if(overlay instanceof Polygon) {
			polygons.add((Polygon) overlay);
		}
		else if(overlay instanceof Circle) {
			circles.add((Circle) overlay);
		}
		else if(overlay instanceof Rectangle) {
			rectangles.add((Rectangle) overlay);
		}
	}

	public void removeMatches(OverlayUUIDRaw uuidRaw){
		OverlayType overlayType = uuidRaw.getOverlayType();
		if(overlayType == null){
			removeMatches(markers,uuidRaw);
			removeMatches(circles,uuidRaw);
			removeMatches(polygons,uuidRaw);
			removeMatches(polylines,uuidRaw);
			removeMatches(rectangles,uuidRaw);
		} else {
			if(overlayType == OverlayType.MARKER){
				removeMatches(markers,uuidRaw);
			} else if(overlayType == OverlayType.CIRCLE){
				removeMatches(circles,uuidRaw);
			} else if(overlayType == OverlayType.POLYGON){
				removeMatches(polygons,uuidRaw);
			} else if(overlayType == OverlayType.POLYLINE){
				removeMatches(polylines,uuidRaw);
			} else if(overlayType == OverlayType.RECTANGLE){
				removeMatches(rectangles,uuidRaw);
			}
		}
	}

	private <T extends Overlay> void removeMatches(List<T>overlays, OverlayUUIDRaw uuidRaw){
		Iterator iterator = overlays.iterator();
		while(iterator.hasNext()){
			Overlay overlay = (Overlay) iterator.next();
			if(uuidRaw.matches(overlay.getId())){
				iterator.remove();
			}
		}
	}

	@SuppressWarnings("unchecked")
    @Deprecated
	public Overlay findOverlay(String id) {
		List list = getOverlayList(id);
		
		if(list != null){
			for(Iterator iterator = list.iterator(); iterator.hasNext();) {
				Overlay overlay = (Overlay) iterator.next();
				if(overlay.getId().equals(id))
					return overlay;
			}
		}
		return null;
	}

	private List<Overlay> getOverlayList(String uuid){
		List list = null;
		
		if(uuid.startsWith(OverlayType.MARKER.toString()))
			list = markers;
		else if(uuid.startsWith(OverlayType.POLYLINE.toString()))
			list = polylines;
		else if(uuid.startsWith(OverlayType.POLYGON.toString()))
			list = polygons;
		else if(uuid.startsWith(OverlayType.CIRCLE.toString()))
			list = circles;
		else if(uuid.startsWith(OverlayType.RECTANGLE.toString()))
			list = rectangles;
		
		return list;
	}
	

	public static String center(LatLng latLng){
		return   latLng.getLat()
			   + ", "
			   + latLng.getLng();
	}

	public static String center(double lat, double lng){
		return   lat
			   + ", "
			   + lng;
	}

	public static String center(Location location){
		return location.getLatitude()
			   + ", "
			   + location.getLongitude();
	}

	public void clear(){
		markers.clear();
		polylines.clear();
		polygons.clear();
		circles.clear();
		rectangles.clear();
	}
	
	public static class GoogleMapModelBuilder{
		private List<Circle>circleList = new ArrayList<Circle>();
		private List<Marker>markerList = new ArrayList<Marker>();
		private List<Polygon>polygonList = new ArrayList<Polygon>();
		private List<Polyline>polylineList = new ArrayList<Polyline>();
		private List<Rectangle>rectangleList = new ArrayList<Rectangle>();
		
		public GoogleMapModelBuilder(){}
		
		public GoogleMapModelBuilder circles(List<Circle>circles){
			this.circleList = circles;
			return this;
		}
		
		public GoogleMapModelBuilder markers(List<Marker>markers){
			this.markerList = markers;
			return this;
		}
		
		public GoogleMapModelBuilder polygon(List<Polygon>polygons){
			this.polygonList = polygons;
			return this;
		}
		
		public GoogleMapModelBuilder polylines(List<Polyline>polylines){
			this.polylineList = polylines;
			return this;
		}
		
		public GoogleMapModelBuilder rectangles(List<Rectangle>rectangles){
			this.rectangleList = rectangles;
			return this;
		}
		
		public GoogleMap build(){
			return new GoogleMap(
					markerList,
					polylineList,
					polygonList,
					circleList,
					rectangleList
					);
		}
	}
}
