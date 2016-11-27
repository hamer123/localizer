package com.pw.localizer.controller;

import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.pw.localizer.controller.google.GoogleMapController;
import com.pw.localizer.identyfikator.OverlayUUIDRaw;
import com.pw.localizer.jsf.JsfMessageBuilder;
import com.pw.localizer.model.entity.*;
import com.pw.localizer.model.enums.AreaMailMessageMode;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.model.enums.OverlayType;
import com.pw.localizer.model.enums.Provider;
import com.pw.localizer.model.google.GoogleMap;
import com.pw.localizer.service.area.AreaService;
import com.pw.localizer.service.area.event.AreaEventService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.map.Polygon;
import com.pw.localizer.factory.CircleFactory;
import com.pw.localizer.factory.MarkerFactory;
import com.pw.localizer.factory.PolygonFactory;
import com.pw.localizer.model.session.LocalizerSession;
import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.repository.area.message.AreaMessageMailRepository;
import com.pw.localizer.repository.area.AreaRepository;
import com.pw.localizer.repository.area.AreaPointRepository;

@Getter
@Setter
@ViewScoped
@Named("areaEventController")
public class AreaEventController implements Serializable{
	@Inject
	private LocalizerSession localizerSession;
	@Inject
	private AreaRepository areaRepository;
	@Inject
	private AreaPointRepository areaPointRepository;
	@Inject
	private GoogleMapController googleMapController;
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
	@Inject
	private AreaEventService areaEventService;

    /** selected area by user */
	private Area selectedArea;

    /** user areas */
	private List<Area> areas;

    /** founded area events */
	private List<AreaEvent>areaEvents;

    /** Date from when we search area events */
	private Date from;

    /** Date to when we search area events */
	private Date to;

	/** Which type of events clear */
	private AreaMessageType areaMessageTypeToClear;

	/** uuid raw to remove markers from gmap */
	private static final OverlayUUIDRaw removeMarker = OverlayUUIDRaw.builder().overlay(OverlayType.MARKER).build();

	/** uuid raw to remove circles from gmap */
	private static final OverlayUUIDRaw removeCircle = OverlayUUIDRaw.builder().overlay(OverlayType.CIRCLE).build();

	/** max results area event for query */
	private static final int MAX_AREA_EVENT_RESULTS = 1000;

	/** days back from now to search latest events */
	private static final int DAYS_BACK_SEARCH_LATEST_AREA_EVENTS = 31;
	
	@PostConstruct
	private void postConstruct(){
		long id = localizerSession.getUser().getId();
		areas = areaRepository.findByProviderIdEagerFetchPoints(id);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////    ACTIONS     //////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Find selected area events and sort them, after that clear google map, create and add polygon to google map
	 * @param event
	 */
	public void onAreaSelect(SelectEvent event) {
		selectedArea = (Area)event.getObject();
		areaEvents = findLatestAreaEvent(selectedArea);
		sortByDateDesc(areaEvents);
		googleMapController.clear();
		Polygon polygon = polygonFactory.create(selectedArea);
		googleMapController.addOverlay(polygon);
		googleMapController.setCenter(GoogleMap.center(polygon.getPaths().get(0)));
	}

	/**
	 * Display location on google map
	 * @param areaEvent
	 */
	public void onDisplayLocation(AreaEvent areaEvent) {
		googleMapController.getGoogleMap().removeMatches(removeMarker);
		googleMapController.getGoogleMap().removeMatches(removeCircle);
		Location location = areaEvent.getLocation();
		googleMapController.addOverlay(circleFactory.createCircle(location));
		googleMapController.addOverlay(markerFactory.createMarker(location));
		googleMapController.setCenter(GoogleMap.center((location)));
	}

	/**
	 * Accept area event notification
	 */
	public void acceptAreaEventNotification() {
		AreaMessageMail areaMessageMail = selectedArea.getAreaMessageMail();
		areaMessageMail.setAccept(true);
		areaMessageMailRepository.save(areaMessageMail);
		JsfMessageBuilder.infoMessage("Zaakceptowano powiadomienie o evencie obszaru śledzenia!");
	}

	/**
	 * Clean area event history
	 */
	public void cleanAreaEventHistory() {
		if(areaMessageTypeToClear == AreaMessageType.ALL) {
			areaEventService.remove(selectedArea.getId());
		} else if(areaMessageTypeToClear == AreaMessageType.GPS) {
			areaEventService.remove(selectedArea.getId(), Provider.GPS);
		} else if(areaMessageTypeToClear == AreaMessageType.NETWORK_NASZ) {
			areaEventService.remove(selectedArea.getId(), LocalizationService.NASZ);
		} else if(areaMessageTypeToClear == AreaMessageType.NETWORK_OBCY) {
			areaEventService.remove(selectedArea.getId(), LocalizationService.OBCY);
		}
		// update area events
		areaEvents = findLatestAreaEvent(selectedArea);
		JsfMessageBuilder.infoMessage("Usunięto rekordy!");
	}

	/**
	 * Search area events for selected area using provided dates
	 */
	public void searchAreaEventFromToDate() {
		areaEvents = areaEventService.getByAreaAndOlderAndYounger(selectedArea.getId(), from, to, MAX_AREA_EVENT_RESULTS);
	}

	/**
	 * Check if area event clear action is allowed
	 * @return
	 */
	public boolean disableAreaEventsClear(){
		return areaEvents.isEmpty();
	}

	/**
	 * Check if area event approve action is allowed
	 * @return
	 */
	public boolean disableAreaEventApprove() {
		if(selectedArea == null) {
			if(!selectedArea.getAreaMessageMail().isAccept() && selectedArea.getAreaMessageMail().getAreaMailMessageMode() == AreaMailMessageMode.ACCEPT) {
				return false;
			}
		}
		return true;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////    UTILITIS    //////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void sortByDateDesc(List<AreaEvent>areaEvents) {
		Collections.sort(areaEvents, (a1,a2) -> a1.getLocation().getDate().compareTo(a2.getLocation().getDate()));
	}

	private List<AreaEvent> findLatestAreaEvent(Area area) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, DAYS_BACK_SEARCH_LATEST_AREA_EVENTS);
		Date yesterday = cal.getTime();
		return areaEventService.getByAreaAndOlderAndYounger(area.getId(), yesterday, new Date(), MAX_AREA_EVENT_RESULTS);
	}

	public enum AreaMessageType {
		ALL,GPS,NETWORK_NASZ,NETWORK_OBCY;
	}
}
