package com.pw.localizer.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.pw.localizer.controller.google.GoogleMapController;
import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.enums.Provider;
import com.pw.localizer.model.google.GoogleMap;
import com.pw.localizer.service.area.AreaService;
import org.omnifaces.cdi.Param;
import org.primefaces.model.map.Polygon;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.AreaPoint;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.repository.area.event.AreaEventGPSRepository;
import com.pw.localizer.repository.area.event.AreaEventNetworkRepository;
import com.pw.localizer.repository.area.AreaPointRepository;

@ViewScoped
@Named
public class AreaEventControllerTODO implements Serializable{
	@Inject
	private AreaEventGPSRepository areaEventGPSRepository;
	@Inject
	private AreaEventNetworkRepository areaEventNetworkRepository;
	@Inject
	private AreaPointRepository areaPointRepository;
//	@Inject
//	private GoogleMapUserComponentService googleMapUserComponentService;
	@Inject
	private GoogleMapController googleMapController;
	@Inject
	private AreaService areaService;
	
	@Param(name = "id") @Inject
	private long id;
	@Param(name = "provider") @Inject
	private Provider provider;
	
	private String msgError;
	private AreaEvent areaEvent;
	private boolean validParams;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   ACTIONS   //////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@PostConstruct
	public void postConstruct(){
		if(validParameters()){
			areaEvent = findEvent();
			if(validateAreaEvent()){
				Area area = areaService.fetchPoints(areaEvent.getArea());
				areaEvent.setArea(area);

				//TODO
				Polygon polygon = null;
				
				googleMapController.setCenter(GoogleMap.center(areaEvent.getLocation()));
				validParams = true;
			}
		}
	}
	
	public void onPokazLokacje(){
		Location location = areaEvent.getLocation();
		googleMapController.setCenter(GoogleMap.center(location));
	}
	
	public void onPokazObszar(){
		AreaPoint point = areaEvent.getArea()
		                           .getPoints()
		                           .get(0);
		googleMapController.setCenter(GoogleMap.center(point.getLat(), point.getLng()));
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////   UTILITIS   //////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	boolean validParameters(){
		if(id == 0){
			msgError = "Podany id nie jest poprawny";
			return false;
		}
		
		if(provider == null){
			msgError = "Podany provider nie jest poprawny";
			return false;
		}
		
		return true;
	}
	
	boolean validateAreaEvent(){
		if(areaEvent == null){
			msgError = "Nie znaleziono rekordu w bazie danych";
			return false;
		}
		return true;
	}
	
	AreaEvent findEvent(){
		if(provider == Provider.GPS)
			return areaEventGPSRepository.findById(id);
		else
			return areaEventNetworkRepository.findById(id);
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////    GETTERS SETTERS     /////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AreaEventGPSRepository getAreaEventGPSRepository() {
		return areaEventGPSRepository;
	}

	public void setAreaEventGPSRepository(AreaEventGPSRepository areaEventGPSRepository) {this.areaEventGPSRepository = areaEventGPSRepository;}

	public AreaEventNetworkRepository getAreaEventNetworkRepository() {
		return areaEventNetworkRepository;
	}

	public void setAreaEventNetworkRepository(AreaEventNetworkRepository areaEventNetworkRepository) { this.areaEventNetworkRepository = areaEventNetworkRepository; }

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public AreaEvent getAreaEvent() {return areaEvent;}

	public void setAreaEvent(AreaEvent areaEvent) {
		this.areaEvent = areaEvent;
	}

	public boolean isValidParams() {
		return validParams;
	}

	public void setValidParams(boolean validParams) {
		this.validParams = validParams;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public AreaPointRepository getAreaPointRepository() {
		return areaPointRepository;
	}

	public void setAreaPointRepository(AreaPointRepository areaPointRepository) {this.areaPointRepository = areaPointRepository;}

	public GoogleMapController getGoogleMapController() {
		return googleMapController;
	}

	public void setGoogleMapController(GoogleMapController googleMapController) { this.googleMapController = googleMapController;}
}
