package com.pw.localizer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.pw.localizer.google.controller.GoogleMapController;
import com.pw.localizer.service.area.AreaPointService;
import com.pw.localizer.service.area.AreaService;
import org.jboss.logging.Logger;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polygon;

import com.pw.localizer.jsf.utilitis.JsfMessageBuilder;
import com.pw.localizer.factory.PolygonFactory;
import com.pw.localizer.model.google.GoogleMapModel;
import com.pw.localizer.model.session.LocalizerSession;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.AreaMessageMail;
import com.pw.localizer.model.entity.AreaPoint;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.repository.area.AreaRepository;
import com.pw.localizer.repository.area.AreaPointRepository;
import com.pw.localizer.repository.user.UserRepository;

@ViewScoped
@Named(value="createArea")
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

	/** */
	private Polygon polygon;

	/** */
	private Area area;

	/** */
	private List<Area> areas = new ArrayList<Area>();

	/** */
	private List<String>targetLoginList;

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
				JsfMessageBuilder.infoMessage("Udalo sie utworzyc obszar sledzenia o nazwie " + area.getName() + " sledzacy uzytkownika " + area.getTarget().getLogin());
			}
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("Nie udalo sie utworzyc obszaru sledzenia " + e);
		}
	}

	/** Change area active on opposite */
	public void onChangeAreaActiveStatus(Area area){
		try{	
			if(area.isActive()){
				areaService.updateActive(area.getId(),false);
				area.setActive(false);
				JsfMessageBuilder.infoMessage("Udalo sie dezaktywowac obszar sledzenia");
			} else {
				areaService.updateActive(area.getId(),true);
				area.setActive(true);
				JsfMessageBuilder.infoMessage("Udalo sie aktywowac obszar sledzenia");
			}
		} catch(Exception e) {
			JsfMessageBuilder.errorMessage("Nie udalo sie zmienic stanu aktywnosci obszaru sledzenia");
			logger.error("[AreaController] Nie udalo sie zmienic stanu aktywnosci obszaru sledzenia " + e);
		}
	}

	/** Remove area */
	public void onRemoveArea(Area area){
		try{
			areaService.remove(area.getId());
			areas.remove(area);
			JsfMessageBuilder.infoMessage("Udalo sie usunac polygon");
		} catch(Exception e) {
			logger.error("[AreaController] Nie udalo sie usunac PolygonModel dla id: " + area.getId());
			JsfMessageBuilder.errorMessage("Nie udalo sie usunac polygon");
		}
	}

	private boolean validateAreaBeforeCreate(){
		boolean valid = true;
		if(isAreaWithName(area.getName())){
			JsfMessageBuilder.errorMessage("Obszar sledzenia o tej nazwie juz istnieje... zmien nazwe");
			valid =  false;
		}
		if(polygon.getPaths().size() < 2){
			JsfMessageBuilder.errorMessage("Obszar sledzenia musi posiadac przynaimej 2 punkty");
			valid =  false;
		}
		if(area.getTarget().getLogin().equals(localizerSession.getUser().getLogin())){
			JsfMessageBuilder.errorMessage("Obszar sledzenia nie moze byc ustawiony na sledzenie providera");
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
		googleMapController.setCenter(GoogleMapModel.center(latLng));
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
	
	public List<String> getListTargetsId() {
		return targetLoginList;
	}

	public void setListTargetsId(List<String> listTargetsId) {
		this.targetLoginList = listTargetsId;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public LocalizerSession getLocalizerSession() {
		return localizerSession;
	}

	public void setLocalizerSession(LocalizerSession localizerSession) {
		this.localizerSession = localizerSession;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public AreaRepository getAreaRepository() {
		return areaRepository;
	}

	public void setAreaRepository(AreaRepository areaRepository) {
		this.areaRepository = areaRepository;
	}

	public AreaPointRepository getPolygonPointRepository() {
		return polygonPointRepository;
	}

	public void setPolygonPointRepository(AreaPointRepository polygonPointRepository) {
		this.polygonPointRepository = polygonPointRepository;
	}

	public GoogleMapController getGoogleMapController() {
		return googleMapController;
	}

	public void setGoogleMapController(GoogleMapController googleMapController) {
		this.googleMapController = googleMapController;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

	public List<String> getTargetLoginList() {
		return targetLoginList;
	}

	public void setTargetLoginList(List<String> targetLoginList) {
		this.targetLoginList = targetLoginList;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

}
