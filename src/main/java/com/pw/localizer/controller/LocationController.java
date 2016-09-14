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

import com.pw.localizer.google.map.UserComponentVisibility;
import com.pw.localizer.google.map.UserGoogleMapController;
import com.pw.localizer.identyfikator.OverlayUUIDConverter;
import com.pw.localizer.identyfikator.OverlayUUIDRaw;
import com.pw.localizer.model.entity.*;
import com.pw.localizer.model.enums.Overlays;
import com.pw.localizer.model.enums.Providers;
import com.pw.localizer.service.UserService;
import org.jboss.logging.Logger;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;

import com.pw.localizer.jsf.utilitis.JsfMessageBuilder;
import com.pw.localizer.model.google.map.GoogleMapComponentVisible;
import com.pw.localizer.model.google.map.GoogleMapModel;
import com.pw.localizer.model.session.LocalizerSession;
import com.pw.localizer.model.google.component.GoogleLocation;
import com.pw.localizer.model.enums.LocalizationServices;
import com.pw.localizer.repository.AreaEventGPSRepository;
import com.pw.localizer.repository.AreaEventNetworkRepository;
import com.pw.localizer.repository.AreaRepository;
import com.pw.localizer.repository.CellInfoMobileRepository;
import com.pw.localizer.repository.UserRepository;
import com.pw.localizer.repository.WifiInfoRepository;
import com.pw.localizer.serivce.qualifier.DialogGMap;
import com.pw.localizer.singleton.RestSessionManager;

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
	private Logger logger;
	
	static final String GOOGLE_MAP_STYLE_MIN_WIDTH = "googleMapMin";
	static final String GOOGLE_MAP_STYLE_MAX_WIDTH = "googleMapMax";
	static final long ONE_MINUTE = 1000 * 60;
	
	private String googleMapStyle = GOOGLE_MAP_STYLE_MIN_WIDTH;
	private String login = "";
	private Location selectLocation;
	private Location locationToDisplayDetails;
	private User selectUserForLastLocations;

	private Map<String,User>users = new HashMap<String,User>();
	private boolean showAreaEventMessage;
	private boolean updateUserAreasOnPolling;

	private UserComponentVisibility userComponentVisibility;
	/** User to display his data in dialog */
	private User userData;
	/** User`avatar in action select overlay */
	private Avatar userAvatar;
	/** User`location in action select overlay */
	private Location userLocation;
	/** User`overlay in action select overlay*/
	private Overlay userOverlay;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////  ACTIONS   ////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@PostConstruct
	public void init(){
//		this.userGoogleMapController.
//		activeAreaIds.addAll( areaRepository.findIdByProviderIdAndActive(localizerSession.getUser().getId(), true) );
		showAreaEventMessage = true;
	}

	/** Add user to follow list */
	public void onAddUserToFollow(){
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

	/** Pobierz nowe lokacje sledzonych uzytknikow i zaaktualizuj google map */
	public void onPoll(){
		updateUserComponents();
		displayAreaEventsMessages();
	}

	/** Wyswietl informacje w ramach zdarzen dla aktywnych aren sledzenia uzytkownikwo */
	private void displayAreaEventsMessages(){
//		if(isShowAreaEventMessage()) {
//			List<AreaEvent> areaEvents = checkAreaEvent();
//			for (AreaEvent areaEvent : areaEvents)
//				JsfMessageBuilder.infoMessage(areaEvent.getMessage());
//		}
	}

	/** Aktualizuje dane uzytkoniwka i google map */
	private void updateUserComponents(){
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
			JsfMessageBuilder.errorMessage("Nie udało się odnowić lokalizacji i zaktualizowac google map");
			e.printStackTrace();
		}
	}

	private User updateUserLastLocations(String login){
		User user = this.users.get(login);
		user.setLastLocationGPS((LocationGPS) this.userRepository.findLastGpsLocationByUserId(user.getId()));
		user.setLastLocationNetworkNaszaUsluga((LocationNetwork) this.userRepository.findLastNetworkNaszLocationByUserId(user.getId()));
		user.setLastLocationNetworObcaUsluga((LocationNetwork) this.userRepository.findLastNetworkObcyLocationByUserId(user.getId()));
		return user;
	}

	private User updateUserAreas(String login){
		User user = this.users.get(login);
		this.userService.getUserAreasFetchAreaPoints(user.getId());
		return user;
	}

	public void onRemoveUserFromFollow(User user){
		try{
			users.remove(user.getLogin());
			userGoogleMapController.remove(user.getLogin());
			JsfMessageBuilder.infoMessage("Udało się usunąć uzytkownika z listy śledzenia");
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Nie udało się usunąć uzytkownika z listy śledzenia");
			e.printStackTrace();
		}
	}
	
	public void onChangeSetting(){
		if(!users.isEmpty()); //tODO
	}
	
	public void onShowLocation(){
		String center = GoogleMapModel.center(selectLocation);
		userGoogleMapController.setCenter(center);
	}
	
	public List<String> onAutocompleteLogin(String login){
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
		//TODO
		List<GoogleLocation> googleLocations = null;//googleMapUserComponentService.lastLocations(user, GoogleMapComponentVisible.NO_POLYGON);
		
		googleMapSingleUserDialogController.clear();
		
		for(GoogleLocation googleLocation : googleLocations)
			googleMapSingleUserDialogController.addOverlay(googleLocation.overlays());
		
		if(!googleLocations.isEmpty())
			googleMapSingleUserDialogController.setCenterIfLocationExist(googleLocations.get(0).getMarker());
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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////  UTILITIS  /////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	boolean isUserArleadyOnList(String login){
		for(String key : users.keySet())
			if(key.equals(login))
				return true;
		
		return false;
	}
	
	<T> void addToListIfNotNull(List<T>list, T obj){
		if(obj != null)
			list.add(obj);
	}
	
	List<String> filterLogins(List<String>logins){
		List<String> filter = logins();
		return logins.stream()
				     .filter(s -> !(filter.contains(s)))
				     .collect(Collectors.toList());
	}

	List<Polygon> createPolygon(User user){
		List<Polygon>polygons = new ArrayList<>();
		
		for(Area area : user.getAreas()){
			//TODO
			Polygon polygon = null; // googleMapUserComponentService.polygon(area, googleMapVisible);
			if(polygon != null)
				polygons.add(polygon);
			}
		return polygons;
	}
	
//	List<AreaEvent> checkAreaEvent(){
//		List<AreaEvent>areaEvents = new ArrayList<>();
//		Date from = new Date(new Date().getTime() - ONE_MINUTE);
//		for(long id : activeAreaIds){
//			areaEvents.addAll( areaEventGPSRepository.findByAreaIdAndDate(id, from) );
//			areaEvents.addAll( areaEventNetworkRepository.findByAreaIdAndDate(id, from) );
//		}
//		return areaEvents;
//	}

	public List<String> usersOnline(){
		return restSessionManager.getUserOnlineLogins();
	}
	
	public List<String>logins(){
		return users.values()
				    .stream()
		            .map(u -> u.getLogin())
		            .collect(Collectors.toList());
	}
	
	public List<Location> selectedUserLocations(){
		List<Location>locations = new ArrayList<Location>();
		addToListIfNotNull(locations, selectUserForLastLocations.getLastLocationGPS());
		addToListIfNotNull(locations, selectUserForLastLocations.getLastLocationNetworkNaszaUsluga());
		addToListIfNotNull(locations, selectUserForLastLocations.getLastLocationNetworObcaUsluga());	
		return locations;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////  GETTERS SETTERS  ///////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
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

	public Location getSelectLocation() {
		return selectLocation;
	}

	public void setSelectLocation(Location selectLocation) {
		this.selectLocation = selectLocation;
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
}
