package com.pw.localizer.controller;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import com.pw.localizer.factory.CircleFactory;
import com.pw.localizer.factory.MarkerFactory;
import com.pw.localizer.controller.google.DialogUserLocationGoogleMapController;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.model.google.UserComponentVisibility;
import com.pw.localizer.controller.google.UserGoogleMapController;
import com.pw.localizer.identyfikator.OverlayUUIDConverter;
import com.pw.localizer.identyfikator.OverlayUUIDRaw;
import com.pw.localizer.inceptor.DurationLogging;
import com.pw.localizer.model.entity.*;
import com.pw.localizer.model.enums.OverlayType;
import com.pw.localizer.model.enums.Provider;
import com.pw.localizer.model.general.UserAdvanceSearch;
import com.pw.localizer.service.area.event.AreaEventService;
import com.pw.localizer.service.user.UserService;
import lombok.Getter;
import lombok.Setter;
import org.jboss.logging.Logger;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import com.pw.localizer.jsf.JsfMessageBuilder;
import com.pw.localizer.model.google.GoogleMapComponentVisible;
import com.pw.localizer.model.google.GoogleMap;
import com.pw.localizer.model.session.LocalizerSession;
import com.pw.localizer.repository.area.AreaRepository;
import com.pw.localizer.repository.location.CellInfoMobileRepository;
import com.pw.localizer.repository.user.UserRepository;
import com.pw.localizer.repository.location.WifiInfoRepository;
import com.pw.localizer.controller.google.DialogGMap;
import com.pw.localizer.singleton.RestSessionManager;

@Getter
@Setter
@ViewScoped
@Named(value="location")
public class LocationController implements Serializable{
	private static final long serialVersionUID = -5534429129019431383L;
	@Inject @DialogGMap
	private DialogUserLocationGoogleMapController googleMapSingleUserDialogController;
	@Inject
	private UserGoogleMapController userGoogleMapController;
	@Inject
	private UserRepository userRepository;
	@Inject
	private WifiInfoRepository wifiInfoRepository;
	@Inject
	private CellInfoMobileRepository cellInfoMobileRepository;
	@Inject
	private AreaRepository areaRepository;
	@Inject
	private AreaEventService areaEventService;
	@Inject
	private RestSessionManager restSessionManager;
	@Inject
	private LocalizerSession localizerSession;
	@Inject
	private UserService userService;
	@Inject
	private MarkerFactory markerFactory;
	@Inject
	private CircleFactory circleFactory;
	@Inject
	private Logger logger;

	/** Provided login */
	private String login;

	/** Selected Location for one of users from follow list */
	private Location selectedFollowedUserLocation;

	/** Choosen Location to display its details */
	private Location locationToDisplayDetails;

	/** Selected User to dipslay his components in panel */
	private User selectUserForLastLocations;

	/** Map of Login(key) and User(value)*/
	private Map<String,User>users = new HashMap<>();

	/** Should show Area event messages */
	private boolean showAreaEventMessage;

	/** Should update areas on polling */
	private boolean updateUserAreasOnPolling;

	/** User components visibility */
	private UserComponentVisibility userComponentVisibility;

	/** User to display his data in dialog */
	private User userData;

	/** User`avatar in action select factory */
	private Avatar userAvatar;

	/** User`location in action select factory */
	private Location userLocation;

	/** User`factory in action select factory */
	private Overlay userOverlay;

	/** Session owner active areas Id */
	private List<Long> activeAreasId;

	/** From this date we get owner all active areas events */
	private Date areaEventFromDate;

	/** User advance search */
	private UserAdvanceSearch userAdvanceSearch = new UserAdvanceSearch();

	/** Founded users by advance search */
	private List<User> advanceSearchFoundedUsers = new ArrayList<>();

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////  ACTIONS   ////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@PostConstruct
	public void postConstruct(){
		areaEventFromDate = new Date();
		activeAreasId = areaRepository.findIdByProviderIdAndActive(localizerSession.getUser().getId(), true);
		showAreaEventMessage = true;
	}

	/**
	 * Dodaj uzytkownika do listy sledzenia, stworz komponenty i zaaktualizuj google map
	 * @param login
     */
	@DurationLogging
	public void addUserToFollow(String login){
		try{
			if(isUserAlreadyOnList(login)){
				JsfMessageBuilder.errorMessage("Użytkownik jest już na liście");
			} else {
				User user = this.userService.getUserFetchArea(login);
				this.users.put(user.getLogin(),user);
				this.userGoogleMapController.add(user);
				JsfMessageBuilder.infoMessage("Udało się dodać uzytkownika do śledzenia");
			}
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Błąd przy próbie dodania użytkownika");
		}
	}

	/**
	 * Pobierz nowe dane uzytkoniwkow i zaaktualizuj google map,
	 * wyswietl zdarzenia w ramach obszarow sledzenia
     */
	@DurationLogging
	public void poll(){
		updateUsersData();
		updateGoogleMapUserComponents();
		displayAreaEventsMessages();
	}

	/**
	 * Usun uzytkownika z listy sledzenia i google map
	 * @param user
     */
	@DurationLogging
	public void removeUserFromFollow(User user){
		try{
			users.remove(user.getLogin());
			userGoogleMapController.remove(user.getLogin());
			//clear reference if we remove the same user as selected
			if(user.getLogin().equals(selectUserForLastLocations.getLogin()))
				selectUserForLastLocations = null;
			JsfMessageBuilder.infoMessage("Udało się usunąć uzytkownika z listy śledzenia");
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Nie udało się usunąć uzytkownika z listy śledzenia");
		}
	}

	/**
	 * Wyswietl informacje w ramach zdarzen dla aktywnych aren sledzenia uzytkownikow
     */
	 void displayAreaEventsMessages(){
		if(showAreaEventMessage) {
			List<AreaEvent> areaEvents = getNewAreaEvent();
			for (AreaEvent areaEvent : areaEvents)
				JsfMessageBuilder.infoMessage(areaEvent.getMessage());
		}
	}

	/**
	 * Update user data
	 */
	void updateUsersData(){
		for(User user : users.values()) {
			User _user;
			if(updateUserAreasOnPolling) {
				_user = userRepository.findByLoginEagerFetchAll(user.getLogin());
				user.setAreas(_user.getAreas());
			} else {
				_user = userRepository.findByLogin(user.getLogin());
			}
			user.setLastLocationGPS(_user.getLastLocationGPS());
			user.setLastLocationNetworkNaszaUsluga(_user.getLastLocationNetworkNaszaUsluga());
			user.setLastLocationNetworObcaUsluga(_user.getLastLocationNetworObcaUsluga());
		}
	}

	/**
	 * Update google map user components
	 */
	void updateGoogleMapUserComponents() {
		for(User user : users.values()) {
			userGoogleMapController.update(user, OverlayType.MARKER);
			userGoogleMapController.update(user, OverlayType.CIRCLE);
			userGoogleMapController.update(user, OverlayType.POLYLINE);
			if(updateUserAreasOnPolling) {
				userGoogleMapController.update(user, OverlayType.POLYGON);
			}
		}
	}

	/**
	 * Set login on provided from advance search dialog
	 * @param login
	 */
	public void advanceSearchAddUser(String login) {
		this.login = login;
	}

	public void onUserAdvanceSearch(){
		advanceSearchFoundedUsers = userRepository.findByLoginLikeAndEmailLikeAndPhoneLike(userAdvanceSearch);
	}

	public void onShowLocation(){
		String center = GoogleMap.center(selectedFollowedUserLocation);
		userGoogleMapController.setCenter(center);
	}
	
	public List<String> onAutocompleteLoginByAdmin(String login){
		List<String>logins = userRepository.findLoginByLoginLike(login);
		return filterLogins(logins);
	}

	public List<String> onAutoCompleteLoginByUser(String login){
		List<String>logins = userRepository.findLoginByLoginLike(login);
		return filterLogins(logins);
	}
	
	public void onShowUserLastLocations(User user){
		selectUserForLastLocations = user;
	}

	public void onShowPolygonLocation(Area area){
		GoogleMapComponentVisible googleMapComponentVisible = userGoogleMapController.getGoogleMapComponentVisible();
		if(area.isActive() && !googleMapComponentVisible.isActivePolygon() || !area.isActive() && !googleMapComponentVisible.isNotActivePolygon()){
			 JsfMessageBuilder.infoMessage("Wpierw włacz widocznosc obszarow w ustawieniach !");
		} else {
			AreaPoint areaPoint = area.getPoints().get(0);
			String center = GoogleMap.center(areaPoint.getLat(), areaPoint.getLng());
			userGoogleMapController.setCenter(center);
		}
	}
	
	public void onSetLocationToDisplayDetails(Location location){
		if(location instanceof LocationNetwork){
			try{
				LocationNetwork locationNetwork = (LocationNetwork)location;
				locationNetwork.setWifiInfo( wifiInfoRepository.findByLocationId(location.getId()) );
				locationNetwork.setCellInfoMobile( cellInfoMobileRepository.findByLocationId(location.getId()) );
			} catch(NoResultException e){
				JsfMessageBuilder.errorMessage("Nie znaleziono szczegółowych danych na temat lokacji");
			}
		}
		locationToDisplayDetails = location;
	}
	
	public void onShowOnlineUserLastLocations(String login){
		User user = userRepository.findByLogin(login);
		//clear
		googleMapSingleUserDialogController.clear();
		googleMapSingleUserDialogController.getLocations().clear();
		//add
		Location location = user.getLastLocationGPS();
		addLocationToSingleUserGMap(location);
		location = user.getLastLocationNetworkNaszaUsluga();
		addLocationToSingleUserGMap(location);
		location = user.getLastLocationNetworObcaUsluga();
		addLocationToSingleUserGMap(location);
		googleMapSingleUserDialogController.setUserLogin(login);
	}

	private void addLocationToSingleUserGMap(Location location){
		if(location != null){
			googleMapSingleUserDialogController.getLocations().add(location);
			Marker marker = markerFactory.createMarker(location);
			Circle circle = circleFactory.createCircle(location);
			this.googleMapSingleUserDialogController.addOverlay(marker);
			this.googleMapSingleUserDialogController.addOverlay(circle);
		}
	}

	public void onClickUserToDisplayData(User user){
		userData = user;
	}

	public void selectUserComponentVisibility(User user){
		userComponentVisibility = userGoogleMapController.getUserComponentVisibilityMap().get(user.getLogin());
	}

	public void onOverlaySelect(OverlaySelectEvent event){
		userOverlay = event.getOverlay();
		if(userOverlay instanceof Marker) {
			OverlayUUIDRaw uuidRaw = OverlayUUIDConverter.uuidRaw(userOverlay.getId());
			User user = users.get(uuidRaw.getLogin());
			userAvatar = user.getAvatar();
			if(uuidRaw.getProvider() == Provider.GPS){
				userLocation = user.getLastLocationGPS();
			}else{
				if(uuidRaw.getLocalizationService() == LocalizationService.NASZ)
					userLocation = user.getLastLocationNetworkNaszaUsluga();
				else
					userLocation = user.getLastLocationNetworObcaUsluga();
			}
		}
	}

	boolean isUserAlreadyOnList(String login){
		for(String key : users.keySet())
			if(key.equals(login))
				return true;
		return false;
	}

	List<String> filterLogins(List<String>logins){
		List<String> filter = getLogins();
		return logins.stream()
				.filter(s -> !(filter.contains(s)))
				.collect(Collectors.toList());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////  UTILITIS  /////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	<T> void addToListIfNotNull(List<T>list, T obj){
		if(obj != null)
			list.add(obj);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////  GETTERS SETTERS  ///////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** ( There is the chance to omit records ) */
	List<AreaEvent> getNewAreaEvent(){
		List<AreaEvent>areaEvents = new ArrayList<>();
		Date futureQueryDate = new Date();
		for(long areaId : activeAreasId) {
			areaEvents.addAll(areaEventService.getByAreaAndDateOlder(areaId, areaEventFromDate));
		}
		areaEventFromDate = futureQueryDate;
		return areaEvents;
	}

	public List<String> getUsersOnline(){
		return restSessionManager.getUserOnlineLogins();
	}

	public List<String> getLogins(){
		return users.values()
				.stream()
				.map(u -> u.getLogin())
				.collect(Collectors.toList());
	}

	public List<Location> getSelectedUserLastLocations(){
		List<Location>locations = new ArrayList<Location>();
		if(this.selectUserForLastLocations != null){
			addToListIfNotNull(locations, selectUserForLastLocations.getLastLocationGPS());
			addToListIfNotNull(locations, selectUserForLastLocations.getLastLocationNetworkNaszaUsluga());
			addToListIfNotNull(locations, selectUserForLastLocations.getLastLocationNetworObcaUsluga());
		}
		return locations;
	}

	public String getUserAvatarUUID(){
		return userAvatar == null ? "" : userAvatar.getUuid();
	}
}
