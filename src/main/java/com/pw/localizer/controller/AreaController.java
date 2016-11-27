package com.pw.localizer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.pw.localizer.controller.google.GoogleMapController;
import com.pw.localizer.model.google.GoogleMap;
import com.pw.localizer.service.area.AreaPointService;
import com.pw.localizer.service.area.AreaService;
import lombok.Getter;
import lombok.Setter;
import org.jboss.logging.Logger;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polygon;
import com.pw.localizer.jsf.JsfMessageBuilder;
import com.pw.localizer.factory.PolygonFactory;
import com.pw.localizer.model.session.LocalizerSession;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.AreaMessageMail;
import com.pw.localizer.model.entity.AreaPoint;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.repository.area.AreaRepository;
import com.pw.localizer.repository.area.AreaPointRepository;
import com.pw.localizer.repository.user.UserRepository;

@Getter
@Setter
@ViewScoped
@Named(value= "areaController")
public class AreaController implements Serializable{
	@Inject
	private LocalizerSession localizerSession;
	@Inject
	private UserRepository userRepository;
	@Inject
	private AreaRepository areaRepository;
	@Inject
	private AreaService areaService;
	@Inject
	private AreaPointService areaPointService;
	@Inject
	private AreaPointRepository polygonPointRepository;
	@Inject
	private GoogleMapController googleMapController;
	@Inject
	private PolygonFactory polygonFactory;
	@Inject 
	private Logger logger;

	/** Polygon in GMap */
	private Polygon polygon;

	/** Area to save */
	private Area area;

	/** User`areas */
	private List<Area> areas = new ArrayList<Area>();

	@PostConstruct
	private void postConstruct(){
		areas = areaRepository.findByProviderId(localizerSession.getUser().getId());
		polygon = polygonFactory.empty();
		googleMapController.addOverlay(polygon);
		//init area
		area = new Area();
		area.setAreaMessageMail(new AreaMessageMail());
		area.setTarget(new User());
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   ACTIONS   ///////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Create area */
	public void onSaveArea(){
		try{
			if(validateAreaBeforeCreate()){
				prepareAreaBeforeSave();
				//Create
				area = areaService.create(area);
				areas.add(area);
				//Clear
				clearArea();
				clearPolygon();
				JsfMessageBuilder.infoMessage("Udało się utworzyć obszar śledzenia o nazwie " + area.getName() + " sledzący użytkownika " + area.getTarget().getLogin());
			}
		} catch(Exception e) {
			JsfMessageBuilder.infoMessage("Nie udało się utworzyć obszar śledzenia o nazwie " + area.getName() + " sledzący użytkownika " + area.getTarget().getLogin());
			logger.error("Nie udało sie utworzyć obszaru śledzenia " + e);
		}
	}

	/** Change area active on opposite */
	public void onChangeAreaActiveStatus(Area area){
		try{	
			if(area.isActive()){
				areaService.updateActive(area.getId(),false);
				area.setActive(false);
				JsfMessageBuilder.infoMessage("Udało się dezaktywowac obszar śledzenia");
			} else {
				areaService.updateActive(area.getId(),true);
				area.setActive(true);
				JsfMessageBuilder.infoMessage("Udało się aktywować obszar śledzenia");
			}
		} catch(Exception e) {
			JsfMessageBuilder.errorMessage("Nie udało się zmienić stanu aktywności obszaru śledzenia");
			logger.error("Nie udało się zmienić stanu aktywności obszaru śledzenia " + e);
		}
	}

	/** Remove area */
	public void onRemoveArea(Area area){
		try{
			areaService.remove(area.getId());
			areas.remove(area);
			JsfMessageBuilder.infoMessage("Udało sie usunać polygon");
		} catch(Exception e) {
			logger.error("[AreaController] Nie udało się usunąć areny dla id: " + area.getId());
			JsfMessageBuilder.errorMessage("Nie udało się usunąć areny");
		}
	}

	/** Clear area create panel */
	public void onClear(){
		clearArea();
		clearPolygon();
	}

	private boolean validateAreaBeforeCreate(){
		boolean valid = true;
		if(isAreaWithName(area.getName())){
			JsfMessageBuilder.errorMessage("Obszar śledzenia o tej nazwie juz istnieje... zmien nazwe");
			valid =  false;
		}
		if(polygon.getPaths().size() < 2){
			JsfMessageBuilder.errorMessage("Obszar śledzenia musi posiadac przynajmniej 2 punkty");
			valid =  false;
		}
		if(area.getTarget().getLogin().equals(localizerSession.getUser().getLogin())){
			JsfMessageBuilder.errorMessage("Obszar śledzenia nie może być ustawiony na śledzenie siebie");
			valid =  false;
		}
		return valid;
	}
	
	public List<String> onAutoCompleteUser(String userLogin){
		return userRepository.findLoginByLoginLike(userLogin);
	}
	
	public void onShowArea(Area area){
		Area areaToDisplay  = copyArea(area);
		List<AreaPoint>points = areaRepository.findAreaPointsByAreaId(area.getId()); //polygonPointRepository.findByAreaId(area.getId());
		setArea(areaToDisplay);
		List<LatLng>paths =  convertToLatLng(points);
		polygon.setPaths(paths);
		setPolygonFillColor(area.getColor());
	}
	
	public void onPathShow(LatLng latLng){
		googleMapController.setCenter(GoogleMap.center(latLng));
	}
	
	public void onPathRemove(LatLng latLng){
		removePathFromPolygon(latLng);
	}
	
	public void onPointSelect(PointSelectEvent event){
        LatLng latlng = event.getLatLng();
        addPathToPolygon(latlng);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////   UTILITIES   ///////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean isAreaWithName(String name){
		for(Area area : areas)
			if(area.getName().equals(name))
				return true;
		return false;
	}
	
	private void prepareAreaBeforeSave(){
		User user = userRepository.findByLogin(area.getTarget().getLogin());
		area.setTarget(user);
		area.setProvider(localizerSession.getUser());
		area.setPoints(areaPointService.convertToAreaPoint(polygon.getPaths()));
	}

	private void addPathToPolygon(LatLng latLng){
		polygon.getPaths().add(latLng);
	}
	
	private void removePathFromPolygon(LatLng latLng){
		List<LatLng>paths = polygon.getPaths();
		paths.remove(latLng);
	}

	private List<LatLng> convertToLatLng(List<AreaPoint>points){
		List<LatLng>paths = new ArrayList<LatLng>();
		for(AreaPoint point : points){
			LatLng latLng = new LatLng(point.getLat(), point.getLng());
			paths.add(latLng);
		}
		return paths;
	}
	
	private Area copyArea(Area area){
		Area copy = new Area();
		copy.setName(area.getName());
		copy.setColor(area.getColor());
		copy.setAreaFollowType(area.getAreaFollowType());
		AreaMessageMail areaMessageMail = new AreaMessageMail();
		areaMessageMail.setActive(area.getAreaMessageMail().isActive());
		areaMessageMail.setAreaMailMessageMode(area.getAreaMessageMail().getAreaMailMessageMode());
		copy.setAreaMessageMail(areaMessageMail);
		copy.setTarget(area.getTarget());
		copy.setProvider(area.getProvider());
		return copy;
	}
	
	private void setPolygonFillColor(String color){
		if(color.startsWith("#"))
			polygon.setFillColor(color);
		else
			polygon.setFillColor("#" + color);
	}

	private void clearArea(){
		area = new Area();
		area.setName("");
		area.setAreaMessageMail(new AreaMessageMail());
		area.setTarget(new User());
		area.setPoints(new ArrayList());
	}

	void clearPolygon(){
		polygon.setPaths(new ArrayList());
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////  GETTERS SETTERS  ///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<LatLng> getPaths(){
		return polygon.getPaths();
	}

	public String getAreaActiveButtonValue(Area area){
		if(area.isActive())
			return "Dezaktywuj";
		return "Aktywuj";
	}

}
