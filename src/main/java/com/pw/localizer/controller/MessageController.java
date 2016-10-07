package com.pw.localizer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.pw.localizer.google.controller.GoogleMapController;
import com.pw.localizer.model.enums.Provider;
import com.pw.localizer.service.area.AreaService;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.map.Polygon;

import com.pw.localizer.factory.CircleFactory;
import com.pw.localizer.factory.MarkerFactory;
import com.pw.localizer.factory.PolygonFactory;
import com.pw.localizer.model.google.GoogleMapModel;
import com.pw.localizer.model.session.LocalizerSession;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.entity.AreaMessageMail;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.entity.AreaPoint;
import com.pw.localizer.repository.area.event.AreaEventGPSRepository;
import com.pw.localizer.repository.area.event.AreaEventNetworkRepository;
import com.pw.localizer.repository.area.message.AreaMessageMailRepository;
import com.pw.localizer.repository.area.AreaRepository;
import com.pw.localizer.repository.area.AreaPointRepository;

@ViewScoped
@Named("messageController")
public class MessageController implements Serializable{
	@Inject
	private LocalizerSession localizerSession;
	@Inject
	private AreaRepository areaRepository;
	@Inject
	private AreaEventGPSRepository areaEventGPSRepository;
	@Inject
	private AreaEventNetworkRepository areaEventNetworkRepository;
	@Inject
	private AreaPointRepository areaPointRepository;
	@Inject
	private GoogleMapController dialogMap;
	@Inject
	private AreaMessageMailRepository areaMessageMailRepository;

	@Inject
	private PolygonFactory polygonFactory;
	@Inject
	private MarkerFactory markerFactory;
	@Inject
	private CircleFactory circleFactory;

	@Inject
	private AreaService areaService;

	private Area selectedArea;
	private List<Area> areas;
	private List<AreaEvent>areaEvents;
	
	@PostConstruct
	private void postConstruct(){
		long id = localizerSession.getUser().getId();
		areas = areaRepository.findWithEagerFetchPointsAndTargetByProviderId(id);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////    ACTIONS     //////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void onAreaSelect(SelectEvent event){
		selectedArea = (Area)event.getObject();
		areaEvents = findAreaAction(selectedArea);
		sortAreaEventByDate(areaEvents);
	}

	public void onDisplayLocationInDialog(AreaEvent areaEvent){
		dialogMap.clear();
		Location location = areaEvent.getLocation();
		dialogMap.addOverlay(circleFactory.createCircle(location));
		dialogMap.addOverlay(markerFactory.createMarker(location));
		dialogMap.setCenter(GoogleMapModel.center((location)));
	}

	/** Set Dialog GMap Polygon and center using first AreaPoint data*/
	public void onDisplayAreaInDialog(Area area){
		area = areaService.fetchPoints(area);
		replaceDialogGMapPolygon(area);
		AreaPoint areaPoint = area.getPoints().get(0);
		dialogMap.setCenter(GoogleMapModel.center(areaPoint.getLat(), areaPoint.getLng()));
	}
	
	public void onAcceptEvent(){
		AreaMessageMail areaMessageMail = selectedArea.getAreaMessageMail();
		areaMessageMail.setAccept(true);
		areaMessageMailRepository.save(areaMessageMail);
	}

	/** Replace Dialog GMap Polygon */
	void replaceDialogGMapPolygon(Area area){
		dialogMap.clear();
		Polygon polygon = polygonFactory.create(area);
		dialogMap.addOverlay(polygon);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////    UTILITIS    //////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void sortAreaEventByDate(List<AreaEvent>areaEvents){
		areaEvents = areaEvents.stream()
		          .sorted((a1,a2) -> a1.getLocation().getDate().compareTo( a2.getLocation().getDate() ))
		          .collect(Collectors.toList());       
	}

	private Map<Integer, AreaPoint> mapAreaPoint(List<AreaPoint>areaPoints){
		Map<Integer, AreaPoint>map = new HashMap<Integer, AreaPoint>();
		
		for(int i = 0; i < areaPoints.size(); i++){
			map.put(i, areaPoints.get(i));
		}
		
		return map;
	}
	
	public String getLocalicationService(AreaEvent areaEvent){
		Location location = areaEvent.getLocation();
		
		if(location instanceof LocationNetwork){
			LocationNetwork locationNetwork = (LocationNetwork)location;
			return locationNetwork.getLocalizerService().toString();
		}
		
		return null;
	}
	
	private List<AreaEvent> findAreaAction(Area area){
		List<AreaEvent>areaEvents = new ArrayList<AreaEvent>();
		
		long id = area.getId();
		areaEvents.addAll(areaEventGPSRepository.findByAreaId(id));
		areaEvents.addAll(areaEventNetworkRepository.findByAreaId(id));
		
		return areaEvents;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////     GETTERS   SETTERS    //////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<AreaEvent> getAreaEvents() {
		return areaEvents;
	}

	public Area getSelectedArea() {
		return selectedArea;
	}

	public void setSelectedArea(Area selectedPolygonModel) {
		this.selectedArea = selectedPolygonModel;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public LocalizerSession getLocalizerSession() {
		return localizerSession;
	}

	public void setLocalizerSession(LocalizerSession localizerSession) {
		this.localizerSession = localizerSession;
	}

	public AreaRepository getAreaRepository() {
		return areaRepository;
	}

	public void setAreaRepository(AreaRepository areaRepository) {
		this.areaRepository = areaRepository;
	}

	public AreaEventGPSRepository getAreaEventGPSRepository() {
		return areaEventGPSRepository;
	}

	public void setAreaEventGPSRepository(
			AreaEventGPSRepository areaEventGPSRepository) {
		this.areaEventGPSRepository = areaEventGPSRepository;
	}

	public AreaEventNetworkRepository getAreaEventNetworkRepository() {
		return areaEventNetworkRepository;
	}

	public void setAreaEventNetworkRepository(
			AreaEventNetworkRepository areaEventNetworkRepository) {
		this.areaEventNetworkRepository = areaEventNetworkRepository;
	}

	public AreaPointRepository getAreaPointRepository() {
		return areaPointRepository;
	}

	public void setAreaPointRepository(AreaPointRepository areaPointRepository) {
		this.areaPointRepository = areaPointRepository;
	}

	public GoogleMapController getDialogMap() {
		return dialogMap;
	}

	public void setDialogMap(GoogleMapController dialogMap) {
		this.dialogMap = dialogMap;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public void setAreaEvents(List<AreaEvent> areaEvents) {
		this.areaEvents = areaEvents;
	}
	
	public Provider[] providers(){
		return Provider.values();
	}

	public AreaMessageMailRepository getAreaMessageMailRepository() {
		return areaMessageMailRepository;
	}

	public void setAreaMessageMailRepository(
			AreaMessageMailRepository areaMessageMailRepository) {
		this.areaMessageMailRepository = areaMessageMailRepository;
	}
	
}
