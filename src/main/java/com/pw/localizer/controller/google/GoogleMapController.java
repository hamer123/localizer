package com.pw.localizer.controller.google;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import com.pw.localizer.model.entity.UserSetting;
import com.pw.localizer.model.google.GoogleMap;
import com.pw.localizer.model.session.LocalizerSession;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;

import com.pw.localizer.jsf.JsfMessageBuilder;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.singleton.LocalizerProperties;

@Dependent
@Default
public class GoogleMapController implements Serializable{
	private static final long serialVersionUID = -2245271606214880961L;

	@Inject
	private LocalizerProperties localizerProperties;
	@Inject
	private LocalizerSession localizerSession;

	protected GoogleMap googleMap;
	protected int zoom;
	protected String center;
	protected boolean streetVisible;
	protected com.pw.localizer.model.enums.GoogleMap googleMapType;
	protected Overlay lastSelectedOverlay;
	protected boolean displayMessageOnSelectOverlay;
	
	@PostConstruct
	public void postConstruct(){
		GoogleMap googleMap = new GoogleMap();
		this.googleMap = googleMap;
		googleMapType = com.pw.localizer.model.enums.GoogleMap.HYBRID;
		streetVisible = true;

		if(localizerSession == null || localizerSession.getUser() == null){
			center = localizerProperties.GoogleMapDefault().DEFAULT_CENTER;
			zoom = localizerProperties.GoogleMapDefault().DEFAULT_ZOOM;
		} else {
			UserSetting userSetting = this.localizerSession.getUser().getUserSetting();
			this.zoom = userSetting.getgMapZoom();
			this.center = GoogleMap.center(userSetting.getDefaultLatitude(), userSetting.getDefaultLongitude());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   ACTIONS   //////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void addOverlay(List<Overlay>overlays){
		for(Overlay overlay : overlays)
			googleMap.addOverlay(overlay);
	}
	
	public void addOverlay(Overlay overlay){
		googleMap.addOverlay(overlay);
	}
	
	public void replace(List<Overlay>overlays){
		googleMap.clear();
		addOverlay(overlays);
	}
	
	public void clear(){
		googleMap.clear();
	}
	
	public void onGoogleMapStateChange(StateChangeEvent event){
		center = GoogleMap.center(event.getCenter());
        zoom = event.getZoomLevel();
	}
	
	public void onOverlaySelect(OverlaySelectEvent event){
		Object overlay = event.getOverlay();
		lastSelectedOverlay = event.getOverlay();
		
		if(overlay instanceof Marker){
			Location location = (Location) event.getOverlay().getData();
			if(displayMessageOnSelectOverlay)
				JsfMessageBuilder.infoMessage( messageMarker(location) );
		} else if(overlay instanceof Circle){
			Location location = (Location) event.getOverlay().getData();
			if(displayMessageOnSelectOverlay)
				JsfMessageBuilder.infoMessage( messageCircle(location) );
		} else if(overlay instanceof Polygon){
			Area area = (Area) event.getOverlay().getData();
			if(displayMessageOnSelectOverlay)
				JsfMessageBuilder.infoMessage( messagePolygon(area) );	
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////   UTILITIS   //////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	String messagePolygon(Area area){
		return   "Obszar sledzenia: "
			   + area.getName()
			   + " Target: "
			   + area.getTarget().getLogin()
			   + " Typ: "
			   + area.getAreaFollowType()
			   + " Aktywny: "
			   + area.isActive();
	}
	
	String messageCircle(Location location){
		return  messageMarker(location) + 
				" Dokładność: " + 
				location.getAccuracy();
	}

	String messageMarker(Location location){
		return  location.getProviderType() +
				" " + 
	            location.getUser().getLogin() +
	            " " +  
				location.getDate();
	}
	
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////    GETTERS SETTERS     /////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public GoogleMap getGoogleMap() {
		return googleMap;
	}
	
	public void setGoogleMap(GoogleMap googleMap) {
		this.googleMap = googleMap;
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
	public String getCenter() {
		return center;
	}
	
	public void setCenter(String center) {
		this.center = center;
	}
	
	public boolean isStreetVisible() {
		return streetVisible;
	}
	
	public void setStreetVisible(boolean streetVisible) {
		this.streetVisible = streetVisible;
	}
	
	public com.pw.localizer.model.enums.GoogleMap getGoogleMapType() {
		return googleMapType;
	}
	
	public void setGoogleMapType(com.pw.localizer.model.enums.GoogleMap googleMapType) {
		this.googleMapType = googleMapType;
	}

	public Overlay getLastSelectedOverlay() {
		return lastSelectedOverlay;
	}

	public void setLastSelectedOverlay(Overlay lastSelectedOverlay) {
		this.lastSelectedOverlay = lastSelectedOverlay;
	}

	public LocalizerProperties getLocalizerProperties() {
		return localizerProperties;
	}

	public void setLocalizerProperties(LocalizerProperties localizerProperties) {
		this.localizerProperties = localizerProperties;
	}

	public boolean isDisplayMessageOnSelectOverlay() {
		return displayMessageOnSelectOverlay;
	}

	public void setDisplayMessageOnSelectOverlay(
			boolean displayMessageOnSelectOverlay) {
		this.displayMessageOnSelectOverlay = displayMessageOnSelectOverlay;
	}
}
