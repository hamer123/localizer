package com.pw.localizer.model.google.map;

public class GoogleMapComponentVisible {
	
	public static final GoogleMapComponentVisible NO_POLYGON = new GoogleMapComponentVisible();
	
	static
	{
		NO_POLYGON.setActivePolygon(true);
		NO_POLYGON.setNotActivePolygon(true);
		NO_POLYGON.setCircleGps(true);
		NO_POLYGON.setCircleNetworkNasz(true);
		NO_POLYGON.setCircleNetworkObcy(true);
		NO_POLYGON.setMarkerGps(true);
		NO_POLYGON.setMarkerNetworkNasz(true);
		NO_POLYGON.setMarkerNetworkObcy(true);
		NO_POLYGON.setGpsPolyline(true);
		NO_POLYGON.setNetworkNaszPolyline(true);
		NO_POLYGON.setNetworkObcyPolyline(true);
	}
	
	private boolean activePolygon;
	private boolean notActivePolygon;
	private boolean markerGps = true;
	private boolean markerNetworkNasz = true;
	private boolean markerNetworkObcy = true;
	private boolean circleGps = true;
	private boolean circleNetworkNasz = true;
	private boolean circleNetworkObcy = true;
	private boolean gpsPolyline;
	private boolean networkNaszPolyline;
	private boolean networkObcyPolyline;
	
	public boolean isNotActivePolygon() {
		return notActivePolygon;
	}
	public void setNotActivePolygon(boolean notActivePolygon) {
		this.notActivePolygon = notActivePolygon;
	}
	public boolean isActivePolygon() {
		return activePolygon;
	}
	public void setActivePolygon(boolean activePolygon) {
		this.activePolygon = activePolygon;
	}
	public boolean isMarkerGps() {
		return markerGps;
	}
	public void setMarkerGps(boolean markerGps) {
		this.markerGps = markerGps;
	}
	public boolean isMarkerNetworkNasz() {
		return markerNetworkNasz;
	}
	public void setMarkerNetworkNasz(boolean markerNetworkNasz) {
		this.markerNetworkNasz = markerNetworkNasz;
	}
	public boolean isMarkerNetworkObcy() {
		return markerNetworkObcy;
	}
	public void setMarkerNetworkObcy(boolean markerNetworkObcy) {
		this.markerNetworkObcy = markerNetworkObcy;
	}
	public boolean isCircleGps() {
		return circleGps;
	}
	public void setCircleGps(boolean circleGps) {
		this.circleGps = circleGps;
	}
	public boolean isCircleNetworkNasz() {
		return circleNetworkNasz;
	}
	public void setCircleNetworkNasz(boolean circleNetworkNasz) {
		this.circleNetworkNasz = circleNetworkNasz;
	}
	public boolean isCircleNetworkObcy() {
		return circleNetworkObcy;
	}
	public void setCircleNetworkObcy(boolean circleNetworkObcy) {
		this.circleNetworkObcy = circleNetworkObcy;
	}
	public boolean isGpsPolyline() {
		return gpsPolyline;
	}
	public void setGpsPolyline(boolean gpsPolyline) {
		this.gpsPolyline = gpsPolyline;
	}
	public boolean isNetworkNaszPolyline() {
		return networkNaszPolyline;
	}
	public void setNetworkNaszPolyline(boolean networkNaszPolyline) {
		this.networkNaszPolyline = networkNaszPolyline;
	}
	public boolean isNetworkObcyPolyline() {
		return networkObcyPolyline;
	}
	public void setNetworkObcyPolyline(boolean networkObcyPolyline) {
		this.networkObcyPolyline = networkObcyPolyline;
	}

}
