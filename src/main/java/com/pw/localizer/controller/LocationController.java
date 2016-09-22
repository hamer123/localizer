package com.pw.localizer.controller;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.pw.localizer.factory.CircleFactory;
import com.pw.localizer.factory.MarkerFactory;
import com.pw.localizer.google.controller.DialogUserLocationGoogleMapController;
import com.pw.localizer.model.google.UserComponentVisibility;
import com.pw.localizer.google.controller.UserGoogleMapController;
import com.pw.localizer.identyfikator.OverlayUUIDConverter;
import com.pw.localizer.identyfikator.OverlayUUIDRaw;
import com.pw.localizer.inceptor.DurationLogging;
import com.pw.localizer.model.entity.*;
import com.pw.localizer.model.enums.Overlays;
import com.pw.localizer.model.enums.Providers;
import com.pw.localizer.model.utilitis.UserAdvanceSearch;
import com.pw.localizer.service.user.UserService;
import org.jboss.logging.Logger;
import org.omnifaces.util.Ajax;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;

import com.pw.localizer.jsf.utilitis.JsfMessageBuilder;
import com.pw.localizer.model.google.GoogleMapComponentVisible;
import com.pw.localizer.model.google.GoogleMapModel;
import com.pw.localizer.model.session.LocalizerSession;
import com.pw.localizer.model.enums.LocalizationServices;
import com.pw.localizer.repository.area.event.AreaEventGPSRepository;
import com.pw.localizer.repository.area.event.AreaEventNetworkRepository;
import com.pw.localizer.repository.area.AreaRepository;
import com.pw.localizer.repository.location.CellInfoMobileRepository;
import com.pw.localizer.repository.user.UserRepository;
import com.pw.localizer.repository.location.WifiInfoRepository;
import com.pw.localizer.qualifier.DialogGMap;
import com.pw.localizer.singleton.RestSessionManager;

@ViewScoped
@Named(value="location")
public class LocationController implements Serializable{
	private static final long serialVersionUID = -5534429129019431383L;
	static final String GOOGLE_MAP_STYLE_MIN_WIDTH = "googleMapMin";
	static final String GOOGLE_MAP_STYLE_MAX_WIDTH = "googleMapMax";

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
	private AreaEventNetworkRepository areaEventNetworkRepository;
	@Inject
	private AreaEventGPSRepository areaEventGPSRepository;
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

	/** Style CSS for google map */
	private String googleMapStyle = GOOGLE_MAP_STYLE_MIN_WIDTH;

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

	/** Session onwer active areas Id */
	private List<Long> activeAreasId;

	/** From this date we get owner all active areas events */
	private Date areaEventFromDate;

	/** User advance search */
	private UserAdvanceSearch userAdvanceSearch = new UserAdvanceSearch();

	/** Founded users by advance search */
	private List<User> advanceSearchFoundedUsers;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////  ACTIONS   ////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Inicjalizacja poczatkowa
     */
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
			if(isUserArleadyOnList(login)){
				JsfMessageBuilder.errorMessage("Użytkownik jest już na liście");
			} else {
				User user = this.userService.getUserFetchAreas(login);
				this.users.put(user.getLogin(),user);
				this.userGoogleMapController.add(user);
				JsfMessageBuilder.infoMessage("Udało się dodać uzytkownika do śledzenia");
			}
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Błąd przy próbie dodania użytkownika");
			e.printStackTrace();
		}
	}

	/**
	 * Pobierz nowe dane uzytkoniwkow i zaaktualizuj google map,
	 * wyswietl zdarzenia w ramach obszarow sledzenia
     */
	@DurationLogging
	public void poll(){
		updateUserComponents();
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
			e.printStackTrace();
		}
	}

	/**
	 * Wyswietl informacje w ramach zdarzen dla aktywnych aren sledzenia uzytkownikow
     */
	 void displayAreaEventsMessages(){
		if(isShowAreaEventMessage()) {
			List<AreaEvent> areaEvents = getAreaEvents();
			for (AreaEvent areaEvent : areaEvents)
				JsfMessageBuilder.infoMessage(areaEvent.getMessage());
		}
	}

	/** Aktualizuje dane uzytkoniwka i google controller */
	void updateUserComponents(){
		try{
			for(String login : users.keySet()) {
				User user = updateUserLastLocations(login);
				this.userGoogleMapController.update(user, Overlays.MARKER);
				this.userGoogleMapController.update(user,Overlays.CIRCLE);
				this.userGoogleMapController.update(user,Overlays.POLYLINE);
				if(updateUserAreasOnPolling) {
					user = updateUserAreas(login);
					this.userGoogleMapController.update(user,Overlays.POLYGON);
				}
			}
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Nie udało się odnowić lokalizacji i zaktualizowac google controller");
			e.printStackTrace();
		}
	}

	public void onUserAdvanceSearch(){
		advanceSearchFoundedUsers = userRepository.findByLoginLikeAndEmailLikeAndPhoneLike(userAdvanceSearch);
	}

	User updateUserLastLocations(String login){
		User user = this.users.get(login);
		user.setLastLocationGPS((LocationGPS) this.userRepository.findLastGpsLocationByUserId(user.getId()));
		user.setLastLocationNetworkNaszaUsluga((LocationNetwork) this.userRepository.findLastNetworkNaszLocationByUserId(user.getId()));
		user.setLastLocationNetworObcaUsluga((LocationNetwork) this.userRepository.findLastNetworkObcyLocationByUserId(user.getId()));
		return user;
	}

	User updateUserAreas(String login){
		User user = this.users.get(login);
		this.userService.getUserAreasFetchAreaPoints(user.getId());
		return user;
	}
	
	public void onShowLocation(){
		String center = GoogleMapModel.center(selectedFollowedUserLocation);
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
	
	public void onToggleMainPanel(ToggleEvent event){
		if(googleMapStyle.equals(GOOGLE_MAP_STYLE_MIN_WIDTH))
			googleMapStyle = GOOGLE_MAP_STYLE_MAX_WIDTH;
		else
			googleMapStyle = GOOGLE_MAP_STYLE_MIN_WIDTH;
	}
	
	public void onShowPolygonLocation(Area area){
		GoogleMapComponentVisible googleMapComponentVisible = userGoogleMapController.getGoogleMapComponentVisible();
		if(area.isActive() && !googleMapComponentVisible.isActivePolygon() || !area.isActive() && !googleMapComponentVisible.isNotActivePolygon()){
			 JsfMessageBuilder.infoMessage("Wpierw włacz widocznosc obszarow w ustawieniach !");
		} else {
			AreaPoint areaPoint = area.getPoints().get(0);
			String center = GoogleMapModel.center(areaPoint.getLat(), areaPoint.getLng());
			userGoogleMapController.setCenter(center);
		}
	}
	
	public void onSetLocationToDipslayDetails(Location location){
		if(location instanceof LocationNetwork){
			LocationNetwork locationNetwork = (LocationNetwork)location;
			locationNetwork.setWifiInfo( wifiInfoRepository.findByLocationId(location.getId()) );
			locationNetwork.setCellInfoMobile( cellInfoMobileRepository.findByLocationId(location.getId()) );
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
			if(uuidRaw.getProvider() == Providers.GPS){
				userLocation = user.getLastLocationGPS();
			}else{
				if(uuidRaw.getLocalizationService() == LocalizationServices.NASZ)
					userLocation = user.getLastLocationNetworkNaszaUsluga();
				else
					userLocation = user.getLastLocationNetworObcaUsluga();
			}
		}
	}

	boolean isUserArleadyOnList(String login){
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

	public void onRefresh(){}
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
	List<AreaEvent> getAreaEvents(){
		List<AreaEvent>areaEvents = new ArrayList<>();
		for(long id : activeAreasId){
			areaEvents.addAll(areaEventGPSRepository.findByAreaIdAndDate(id, areaEventFromDate));
			areaEvents.addAll(areaEventNetworkRepository.findByAreaIdAndDate(id, areaEventFromDate));
		}
		areaEventFromDate = new Date();
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

	public LocalizationServices getLocalizationServices(Location location){
		if(location instanceof LocationNetwork)
			return ( (LocationNetwork)location ).getLocalizationServices();
		return null;
	}

	public DialogUserLocationGoogleMapController getGoogleMapSingleUserDialogController() {
		return googleMapSingleUserDialogController;
	}

	public Location getLocationToDisplayDetails() {
		return locationToDisplayDetails;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public User getSelectUser() {
		return selectUserForLastLocations;
	}

	public void setSelectUser(User selectUser) {
		this.selectUserForLastLocations = selectUser;
	}

	public Location getSelectedFollowedUserLocation() {
		return selectedFollowedUserLocation;
	}

	public void setSelectedFollowedUserLocation(Location selectedFollowedUserLocation) {
		this.selectedFollowedUserLocation = selectedFollowedUserLocation;
	}

	public String getGoogleMapStyle() {
		return googleMapStyle;
	}

	public boolean isShowAreaEventMessage() {
		return showAreaEventMessage;
	}

	public void setShowAreaEventMessage(boolean showAreaEventMessage) {
		this.showAreaEventMessage = showAreaEventMessage;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public WifiInfoRepository getWifiInfoRepository() {
		return wifiInfoRepository;
	}

	public void setWifiInfoRepository(WifiInfoRepository wifiInfoRepository) {
		this.wifiInfoRepository = wifiInfoRepository;
	}

	public CellInfoMobileRepository getCellInfoMobileRepository() {
		return cellInfoMobileRepository;
	}

	public void setCellInfoMobileRepository(
			CellInfoMobileRepository cellInfoMobileRepository) {
		this.cellInfoMobileRepository = cellInfoMobileRepository;
	}

	public AreaRepository getAreaRepository() {
		return areaRepository;
	}

	public void setAreaRepository(AreaRepository areaRepository) {
		this.areaRepository = areaRepository;
	}

	public AreaEventNetworkRepository getAreaEventNetworkRepository() {
		return areaEventNetworkRepository;
	}

	public void setAreaEventNetworkRepository(
			AreaEventNetworkRepository areaEventNetworkRepository) {
		this.areaEventNetworkRepository = areaEventNetworkRepository;
	}

	public AreaEventGPSRepository getAreaEventGPSRepository() {
		return areaEventGPSRepository;
	}

	public void setAreaEventGPSRepository(
			AreaEventGPSRepository areaEventGPSRepository) {
		this.areaEventGPSRepository = areaEventGPSRepository;
	}

	public RestSessionManager getRestSessionManager() {
		return restSessionManager;
	}

	public void setRestSessionManager(RestSessionManager restSessionManager) {
		this.restSessionManager = restSessionManager;
	}

	public LocalizerSession getLocalizerSession() {
		return localizerSession;
	}

	public void setLocalizerSession(LocalizerSession localizerSession) {
		this.localizerSession = localizerSession;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setGoogleMapSingleUserDialogController(
			DialogUserLocationGoogleMapController googleMapSingleUserDialogController) {
		this.googleMapSingleUserDialogController = googleMapSingleUserDialogController;
	}

	public void setGoogleMapStyle(String googleMapStyle) {
		this.googleMapStyle = googleMapStyle;
	}

	public void setLocationToDisplayDetails(Location locationToDisplayDetails) {
		this.locationToDisplayDetails = locationToDisplayDetails;
	}

	public Map<String, User> getUsers() {
		return users;
	}

	public void setUsers(Map<String, User> users) {
		this.users = users;
	}

	public User getSelectUserForLastLocations() {
		return selectUserForLastLocations;
	}

	public void setSelectUserForLastLocations(User selectUserForLastLocations) {
		this.selectUserForLastLocations = selectUserForLastLocations;
	}

	public User getUserData() {
		return userData;
	}

	public void setUserData(User userData) {
		this.userData = userData;
	}

	public UserComponentVisibility getUserComponentVisibility() {
		return userComponentVisibility;
	}

	public void setUserComponentVisibility(UserComponentVisibility userComponentVisibility) {
		this.userComponentVisibility = userComponentVisibility;
	}

	public Overlay getUserOverlay() {
		return userOverlay;
	}

	public void setUserOverlay(Overlay userOverlay) {
		this.userOverlay = userOverlay;
	}

	public Avatar getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(Avatar userAvatar) {
		this.userAvatar = userAvatar;
	}

	public Location getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(Location userLocation) {
		this.userLocation = userLocation;
	}

	public String getUserAvatarUUID(){
		return userAvatar == null ? "" : userAvatar.getUuid();
	}

	public boolean isUpdateUserAreasOnPolling() {
		return updateUserAreasOnPolling;
	}

	public void setUpdateUserAreasOnPolling(boolean updateUserAreasOnPolling) {
		this.updateUserAreasOnPolling = updateUserAreasOnPolling;
	}

	public UserAdvanceSearch getUserAdvanceSearch() {
		return userAdvanceSearch;
	}

	public void setUserAdvanceSearch(UserAdvanceSearch userAdvanceSearch) {
		this.userAdvanceSearch = userAdvanceSearch;
	}

	public List<User> getAdvanceSearchFoundedUsers() {
		return advanceSearchFoundedUsers;
	}

	public void setAdvanceSearchFoundedUsers(List<User> advanceSearchFoundedUsers) {
		this.advanceSearchFoundedUsers = advanceSearchFoundedUsers;
	}
}
