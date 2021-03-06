package com.pw.localizer.controller;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import com.pw.localizer.factory.MarkerFactory;
import com.pw.localizer.factory.PolylineFactory;
import com.pw.localizer.controller.google.GoogleMapController;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.qualifier.Network;
import com.pw.localizer.service.location.LocationService;
import org.jboss.logging.Logger;

import com.pw.localizer.jsf.JsfMessageBuilder;
import com.pw.localizer.model.google.GoogleMap;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.enums.Provider;
import com.pw.localizer.repository.location.LocationGPSRepository;
import com.pw.localizer.repository.location.LocationNetworkRepository;
import com.pw.localizer.repository.user.UserRepository;
import com.pw.localizer.service.utilitis.RouteService;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

@ViewScoped
@Named("locationHistory")
public class LocationHistoryController implements Serializable{
	@Inject
	private LocationNetworkRepository locationNetworkRepository;
	@Inject
	private LocationGPSRepository locationGPSRepository;
	@Inject
	private UserRepository userRepository;
	@Inject
	private GoogleMapController googleMapController;
	@Inject
	private RouteService routeService;
	@Inject
	private PolylineFactory polylineFactory;
	@Inject
	private MarkerFactory markerFactory;
	@Inject @Network
	LocationService<LocationNetwork> networkLocationService;
	@Inject
	private Logger logger;

	/** Max records in result for locations */
	private int maxRecords = 1000;

	/** Date used in query to get locations */
	private Date from;

	/** Date used in query to get locations */
	private Date to;

	/** login */
	private String login;

	/** Choose provider */
	private Provider provider;

	/** Choose service */
	private LocalizationService localizationService;

	/** Selected location */
	private Location location;

	/** Polyline */
	private Polyline polyline;

	/** Locations */
	private List<Location>locationList = new ArrayList<>();

	/** Selected location to display its details */
	private Location selectedLocation;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   ACTIONS    /////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void onCreateRoute(){
		try{
			//Clear
			polyline = null;
			locationList.clear();
			googleMapController.clear();

			List<Location>locations = findLocations();
			if(locations.size() < 2){
				JsfMessageBuilder.errorMessage("Nie mozna utworzyc sciezki, za malo lokacji");
			} else {
				locationList = locations;
				polyline = polylineFactory.create(locations);
				googleMapController.addOverlay(polyline);
				//add start marker
				Marker startMarker = createStart(locations.get(0));
				googleMapController.addOverlay(startMarker);
				//add end marker
				Marker endMarker = createEnd(locations.get(locations.size() - 1));
				googleMapController.addOverlay(endMarker);
			}
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Nie mozna utworzyc sciezki");
			logger.error(e);
		}
	}
	
	public void onLocationSelect(){
		 googleMapController.setCenter( GoogleMap.center(location) );
	}
	
	public List<String> onAutoCompleteLogin(String login){
		return userRepository.findLoginByLoginLike(login);
	}
	
	public void onCalculateRouteLength(){
		double lengthDouble = routeService.calculateLengthMeters(polyline);
		String length = new DecimalFormat("#.##").format(lengthDouble);
		JsfMessageBuilder.infoMessage(length + " meters");
	}

	public void onSetLocationToDisplayDetails(Location location){
		if(location instanceof LocationNetwork){
			try{
				networkLocationService.fetchRelations((LocationNetwork) location);
			} catch(NoResultException e){
				JsfMessageBuilder.errorMessage("Nie znaleziono szczegółowych danych na temat lokacji");
			}
		}
		selectedLocation = location;
	}

	Marker createStart(Location location){
		return markerFactory.createStartRouteMarker(location);
	}

	Marker createEnd(Location location){
		return markerFactory.createEndRouteMarker(location);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   UTILITIS    ////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private List<Location> findLocations(){
		List<Location>locations = new ArrayList<Location>();
		
		if(provider == Provider.GPS){
			locations.addAll( findLocationGPS() );
		} else {
			locations.addAll( findLocationNetwork() );
		} 
		
		return locations;
	}
	
	private List<LocationNetwork> findLocationNetwork(){
		if(localizationService == LocalizationService.NASZ){
			return locationNetworkRepository.
					findByLoginAndDateForServiceNaszOrderByDate(login, from, to, maxRecords);
		} else {
			return locationNetworkRepository.
					findByLoginAndDateForServiceObcyOrderByDate(login, from, to, maxRecords);
		}
	}
	
	private List<LocationGPS> findLocationGPS(){
		return locationGPSRepository.
				findByLoginAndDateOrderByDate(login, from, to, maxRecords);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   GETTERS SETTERS    /////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Provider[] providers(){
		return Provider.values();
	}
	
	public LocalizationService[] localizationServices(){
		return LocalizationService.values();
	}
	
	public int getMaxRecords() {
		return maxRecords;
	}

	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public String getLogin() {
		return login;
	}
	public GoogleMapController getGoogleMapController() {
		return googleMapController;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	public Provider getProvider() {
		return provider;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	public LocalizationService getLocalizationService() {
		return localizationService;
	}

	public void setLocalizationService(LocalizationService localizationService) {
		this.localizationService = localizationService;
	}

	public LocationNetworkRepository getLocationNetworkRepository() {
		return locationNetworkRepository;
	}

	public void setLocationNetworkRepository(
			LocationNetworkRepository locationNetworkRepository) {
		this.locationNetworkRepository = locationNetworkRepository;
	}

	public LocationGPSRepository getLocationGPSRepository() {
		return locationGPSRepository;
	}

	public void setLocationGPSRepository(LocationGPSRepository locationGPSRepository) {
		this.locationGPSRepository = locationGPSRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public RouteService getRouteService() {
		return routeService;
	}

	public void setRouteService(RouteService routeService) {
		this.routeService = routeService;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setGoogleMapController(GoogleMapController googleMapController) {
		this.googleMapController = googleMapController;
	}

	public PolylineFactory getPolylineFactory() {
		return polylineFactory;
	}

	public void setPolylineFactory(PolylineFactory polylineFactory) {
		this.polylineFactory = polylineFactory;
	}

	public Polyline getPolyline() {
		return polyline;
	}

	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	public Location getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(Location selectedLocation) {
		this.selectedLocation = selectedLocation;
	}
}
