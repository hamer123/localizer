package com.pw.localizer.google.controller;

import com.pw.localizer.identyfikator.OverlayUUIDConverter;
import com.pw.localizer.identyfikator.OverlayUUIDRaw;
import com.pw.localizer.model.entity.*;
import com.pw.localizer.factory.CircleFactory;
import com.pw.localizer.factory.MarkerFactory;
import com.pw.localizer.factory.PolygonFactory;
import com.pw.localizer.factory.PolylineFactory;
import com.pw.localizer.model.enums.GoogleMaps;
import com.pw.localizer.model.enums.LocalizationServices;
import com.pw.localizer.model.enums.Overlays;
import com.pw.localizer.model.enums.Providers;
import com.pw.localizer.model.google.GoogleMapComponentVisible;
import com.pw.localizer.model.google.GoogleMapModel;
import com.pw.localizer.model.google.OverlayCreateFilter;
import com.pw.localizer.model.google.UserComponentVisibility;
import com.pw.localizer.model.session.LocalizerSession;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.*;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wereckip on 08.09.2016.
 */

@Named(value = "userGoogleMap")
@ViewScoped
public class UserGoogleMapController implements Serializable{
    @Inject
    private PolylineFactory polylineFactory;
    @Inject
    private PolygonFactory polygonFactory;
    @Inject
    private MarkerFactory markerFactory;
    @Inject
    private CircleFactory circleFactory;
    @Inject
    private LocalizerSession localizerSession;

    //Gotowy model po renderingu
    private GoogleMapModel googleMapModelOutput = new GoogleMapModel();
    //Zawartosc po utworzeniu
    private GoogleMapModel googleMapModel = new GoogleMapModel();
    //Widocznosc komponentow indywidulanie dla uzytkoniwka
    private Map<String,UserComponentVisibility> userComponentVisibilityMap = new HashMap<>();
    //Ogolna widocznosc komponentow
    private GoogleMapComponentVisible googleMapComponentVisible = new GoogleMapComponentVisible();
    //Ograniczenie tworzenia komponentow
    private OverlayCreateFilter overlayCreateFilter = new OverlayCreateFilter();

    private int zoom;
    private String center;
    private boolean streetVisible = true;
    private GoogleMaps googleMapType = GoogleMaps.TERRAIN;
    private boolean displayMessageOnSelectOverlay = true;
    private Overlay lastSelectedOverlay;

    @PostConstruct
    private void postConstruct(){
        this.googleMapType = GoogleMaps.HYBRID;
        if(this.localizerSession.getUser() == null){

        } else {
            UserSetting userSetting = this.localizerSession.getUser().getUserSetting();
            this.zoom = userSetting.getgMapZoom();
            this.center = GoogleMapModel.center(userSetting.getDefaultLatitude(), userSetting.getDefaultLongtitude());

            this.overlayCreateFilter.setCreateActivePolygon(true);
            this.overlayCreateFilter.setCreateNotActivePolygon(true);
        }
    }

    public void add(User user){
        userComponentVisibilityMap.put(user.getLogin(),new UserComponentVisibility());
        addCircle(user);
        addMarker(user);
        addPolygon(user);
        addPolyline(user);
    }

    public void remove(String login){
        removeCircle(login);
        removeMarker(login);
        removePolygon(login);
        removePolyline(login);
        userComponentVisibilityMap.remove(login);
    }

    public void update(User user, Overlays overlays){
        if(overlays == Overlays.MARKER){
            removeMarker(user.getLogin());
            addMarker(user);
        } else if(overlays == Overlays.CIRCLE){
            removeCircle(user.getLogin());
            addCircle(user);
        } else if(overlays == Overlays.POLYLINE){
            updatePolylines(user);
        } else if(overlays == Overlays.POLYGON){
            removePolygon(user.getLogin());
            addPolygon(user);
        } else {
            throw new RuntimeException("Nie wspierany typ factory dla update " + overlays);
        }
    }

    public void render(){
        renderCircles();
        renderMarkers();
        renderPolygons();
        renderPolylines();
    }

    public void render(Overlays overlay){
        if(overlay == Overlays.MARKER){
            renderMarkers();
        } else if(overlay == Overlays.CIRCLE){
            renderCircles();
        } else if(overlay == Overlays.POLYGON){
            renderPolygons();
        } else if(overlay == Overlays.POLYLINE){
            renderPolylines();
        } else {
            throw new RuntimeException("Nie wspierany factory dla renderingu " + overlay);
        }
    }

    private void addMarker(User user){
        Location location = user.getLastLocationGPS();
        if(location != null)
            addGpsMarker(location);
        location = user.getLastLocationNetworkNaszaUsluga();
        if(location != null)
            addNetworkMarker(location);
        location = user.getLastLocationNetworObcaUsluga();
        if(location != null)
            addNetworkMarker(location);
    }

    private void addGpsMarker(Location location){
        List<Marker>markers = googleMapModel.getMarkers();
        Marker marker = markerFactory.createMarker(location);
        markers.add(marker);
        if(filterGpsMarker(marker, OverlayUUIDConverter.uuidRaw(marker.getId())))
            googleMapModelOutput.getMarkers().add(marker);
    }

    private void addNetworkMarker(Location location){
        List<Marker>markers = googleMapModel.getMarkers();
        Marker marker = markerFactory.createMarker(location);
        markers.add(marker);
        if(filterNetworkMarker(marker,OverlayUUIDConverter.uuidRaw(marker.getId())))
            googleMapModelOutput.getMarkers().add(marker);
    }

    private void addCircle(User user){
        Location location = user.getLastLocationGPS();
        if(location != null)
            addCircleGps(location);

        location = user.getLastLocationNetworkNaszaUsluga();
        if(location != null)
            addCircleNetwork(location);

        location = user.getLastLocationNetworObcaUsluga();
        if(location != null)
            addCircleNetwork(location);
    }

    private void addCircleGps(Location location){
        List<Circle>circles = googleMapModel.getCircles();
        Circle circle = circleFactory.createCircle(location);
        circles.add(circle);
        if(filterCircleGps(circle,OverlayUUIDConverter.uuidRaw(circle.getId())))
            googleMapModelOutput.getCircles().add(circle);
    }

    private void addCircleNetwork(Location location){
        List<Circle>circles = googleMapModel.getCircles();
        Circle circle = circleFactory.createCircle(location);
        circles.add(circle);
        if(filterCircleNetwork(circle,OverlayUUIDConverter.uuidRaw(circle.getId())))
            googleMapModelOutput.getCircles().add(circle);
    }

    /** Polygon data is boolean active variable */
    private void addPolygon(User user){
        List<Polygon>polygons = googleMapModel.getPolygons();
        List<Area>areas = user.getAreas();
        for(Area area : areas){
            if(overlayCreateFilter.isCreateActivePolygon() && area.isActive()){
                Polygon polygon = polygonFactory.create(area, true);
                polygons.add(polygon);
                if(filterPolygon(polygon,OverlayUUIDConverter.uuidRaw(polygon.getId())))
                    googleMapModelOutput.getPolygons().add(polygon);
            } else if(overlayCreateFilter.isCreateNotActivePolygon() && !area.isActive()){
                Polygon polygon = polygonFactory.create(area, false);
                polygons.add(polygon);
                if(filterPolygon(polygon,OverlayUUIDConverter.uuidRaw(polygon.getId())))
                    googleMapModelOutput.getPolygons().add(polygon);
            }
        }
    }

    private void updatePolylines(User user){
        updatePolylineGps(user);
        updatePolylineNetwork(user);
    }

    private void updatePolylineGps(User user){
        Location location = user.getLastLocationGPS();
        OverlayUUIDRaw uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.insatnce()
                .login(user.getLogin())
                .provider(Providers.GPS)
                .build();
        updatePolyline(location,uuidRaw);
    }

    private void updatePolylineNetwork(User user){
        Location location = user.getLastLocationNetworkNaszaUsluga();
        OverlayUUIDRaw uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.insatnce()
                .login(user.getLogin())
                .provider(Providers.NETWORK)
                .localizationService(LocalizationServices.NASZ)
                .build();
        updatePolyline(location,uuidRaw);

        location = user.getLastLocationNetworObcaUsluga();
        uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.insatnce()
                .login(user.getLogin())
                .provider(Providers.NETWORK)
                .localizationService(LocalizationServices.OBCY)
                .build();
        updatePolyline(location,uuidRaw);
    }

    private void updatePolyline(Location location, OverlayUUIDRaw uuidRaw){
        if(location != null){
            Polyline polyline = findPolyline(uuidRaw);
            if(polyline != null){
                if(!polyline.getData().equals(location.getDate())){
                    polyline.getPaths().add(new LatLng(location.getLatitude(), location.getLongitude()));
                    polyline.setData(location.getDate());
                }
            } else {
                //TODO
            }
        }
    }

    private Polyline findPolyline(OverlayUUIDRaw uuidRaw){
        for(Polyline polyline : googleMapModel.getPolylines()){
            if(uuidRaw.matches(polyline.getId()))
                return polyline;
        }
        return null;
    }

    /** Polyline data is location date */
    private void addPolyline(User user){
        Location location = user.getLastLocationGPS();
        if(location != null)
            addPolylineGps(location);

        location = user.getLastLocationNetworkNaszaUsluga();
        if(location != null)
            addPolylineNetwork(location);

        location = user.getLastLocationNetworObcaUsluga();
        if(location != null)
            addPolylineNetwork(location);
    }

    private void addPolylineGps(Location location){
        if(overlayCreateFilter.isCreateGPSPolyline()){
            Polyline polyline = polylineFactory.create(location, location.getDate());
            googleMapModel.getPolylines().add(polyline);
            if(filterPolylineGps(OverlayUUIDConverter.uuidRaw(polyline.getId()))){
                googleMapModelOutput.getPolylines().add(polyline);
            }
        }
    }

    private void addPolylineNetwork(Location location){
        List<Polyline>polylines = googleMapModel.getPolylines();
        LocationNetwork locationNetwork = (LocationNetwork)location;
        if(locationNetwork.getLocalizationServices() == LocalizationServices.NASZ){
            if(overlayCreateFilter.isCreateNetworkNaszPolyline()){
                Polyline polyline = polylineFactory.create(location,location.getDate());
                polylines.add(polyline);
                if(filterPolylineNetwork(OverlayUUIDConverter.uuidRaw(polyline.getId()))){
                    googleMapModelOutput.getPolylines().add(polyline);
                }
            }
        } else if(locationNetwork.getLocalizationServices() == LocalizationServices.OBCY){
            if(overlayCreateFilter.isCreateNetworkObcyPolyline()){
                Polyline polyline = polylineFactory.create(location,location.getDate());
                polylines.add(polyline);
                if(filterPolylineNetwork(OverlayUUIDConverter.uuidRaw(polyline.getId()))){
                    googleMapModelOutput.getPolylines().add(polyline);
                }
            }
        } else {
            throw new RuntimeException("Nie wspierana usluga " + ((LocationNetwork) location).getLocalizationServices());
        }
    }

    private void removeMarker(String login){
        Iterator<Marker>markerIterator = googleMapModel.getMarkers().iterator();
        while(markerIterator.hasNext()){
            Marker marker = markerIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(marker.getId());
            if(login.equals(extractLogin)) {
                markerIterator.remove();
                googleMapModelOutput.getMarkers().remove(marker);
            }
        }
    }

    private void removeCircle(String login){
        Iterator<Circle>circleIterator = googleMapModel.getCircles().iterator();
        while(circleIterator.hasNext()){
            Circle circle = circleIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(circle.getId());
            if(login.equals(extractLogin)) {
                circleIterator.remove();
                googleMapModelOutput.getCircles().remove(circle);
            }
        }
    }

    private void removePolygon(String login){
        Iterator<Polygon>polygonIterator = googleMapModel.getPolygons().iterator();
        while(polygonIterator.hasNext()){
            Polygon polygon = polygonIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(polygon.getId());
            if(login.equals(extractLogin)) {
                polygonIterator.remove();
                googleMapModelOutput.getPolygons().remove(polygon);
            }
        }
    }

    private void removePolyline(String login){
        Iterator<Polyline>polylineIterator = googleMapModel.getPolylines().iterator();
        while(polylineIterator.hasNext()){
            Polyline polyline = polylineIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(polyline.getId());
            if(login.equals(extractLogin)) {
                polylineIterator.remove();
                googleMapModelOutput.getPolylines().remove(polyline);
            }
        }
    }

    private void renderMarkers(){
        List<Marker>markers = googleMapModelOutput.getMarkers();
        markers.clear();

        for(Marker marker : googleMapModel.getMarkers()){
            OverlayUUIDRaw uuidRaw = OverlayUUIDConverter.uuidRaw(marker.getId());
            if(uuidRaw.getProvider() == Providers.GPS){
                if(filterGpsMarker(marker,uuidRaw))
                    markers.add(marker);
            } else if(uuidRaw.getProvider() == Providers.NETWORK){
                if(filterNetworkMarker(marker,uuidRaw))
                    markers.add(marker);
            } else {
                throw new RuntimeException("Nie wspierany provider przy renderowaniu markerow " + uuidRaw.getProvider());
            }
        }
    }

    private boolean filterGpsMarker(Marker marker, OverlayUUIDRaw uuidRaw){
        if(googleMapComponentVisible.isMarkerGps()){
            UserComponentVisibility userComponentVisibility = userComponentVisibilityMap.get(uuidRaw.getLogin());
            return userComponentVisibility.isGPSMarker();
        } else {
            return false;
        }
    }

    private boolean filterNetworkMarker(Marker marker, OverlayUUIDRaw uuidRaw){
        LocalizationServices service = uuidRaw.getLocalizationService();
        UserComponentVisibility userComponentVisibility = userComponentVisibilityMap.get(uuidRaw.getLogin());
        if(service == LocalizationServices.NASZ){
            if(googleMapComponentVisible.isMarkerNetworkNasz())
                return userComponentVisibility.isNetworkNaszMarker();
        } else if(service == LocalizationServices.OBCY){
            if(googleMapComponentVisible.isMarkerNetworkObcy())
                return userComponentVisibility.isNetworkObcyMarker();
        } else {
            throw new RuntimeException("Nie wspierany service przez renderowanie marketow " + uuidRaw.getLocalizationService());
        }

        return false;
    }

    private void renderCircles(){
        List<Circle>circles = googleMapModelOutput.getCircles();
        circles.clear();

        for(Circle circle : googleMapModel.getCircles()){
            OverlayUUIDRaw uuidRaw = OverlayUUIDConverter.uuidRaw(circle.getId());
            if(uuidRaw.getProvider() == Providers.GPS){
                if(filterCircleGps(circle,uuidRaw))
                    circles.add(circle);
            } else if(uuidRaw.getProvider() == Providers.NETWORK){
                if(filterCircleNetwork(circle,uuidRaw))
                    circles.add(circle);
            } else {
                throw new RuntimeException("Nie wspierany provider przez renderowanie circle " + uuidRaw.getProvider());
            }
        }
    }

    private boolean filterCircleGps(Circle circle, OverlayUUIDRaw uuidRaw){
        if(googleMapComponentVisible.isCircleGps()){
            UserComponentVisibility userComponentVisibility = userComponentVisibilityMap.get(uuidRaw.getLogin());
            return userComponentVisibility.isGPSCircle();
        } else {
            return false;
        }
    }

    private boolean filterCircleNetwork(Circle circle, OverlayUUIDRaw uuidRaw){
        LocalizationServices service = uuidRaw.getLocalizationService();
        UserComponentVisibility userComponentVisibility = userComponentVisibilityMap.get(uuidRaw.getLogin());
        if(service == LocalizationServices.NASZ){
            if(googleMapComponentVisible.isCircleNetworkNasz()){
                return userComponentVisibility.isNetworkNaszCircle();
            }
        } else if(service == LocalizationServices.OBCY){
            if(googleMapComponentVisible.isCircleNetworkObcy())
                return userComponentVisibility.isNetworkObcyCircle();
        } else {
            throw new RuntimeException("Nie wspierany serwis przez filterCircleNetwork circle " + uuidRaw.getLocalizationService());
        }

        return false;
    }

    private void renderPolygons(){
        List<Polygon>polygons = googleMapModelOutput.getPolygons();
        polygons.clear();

        for(Polygon polygon : googleMapModel.getPolygons()){
            OverlayUUIDRaw uuidRaw = OverlayUUIDConverter.uuidRaw(polygon.getId());
            if(filterPolygon(polygon,uuidRaw))
                googleMapModelOutput.getPolygons().add(polygon);
        }
    }

    private boolean filterPolygon(Polygon polygon, OverlayUUIDRaw uuidRaw){
        boolean active = (Boolean) polygon.getData();
        String login = uuidRaw.getLogin();
        if(active){
            if(googleMapComponentVisible.isActivePolygon())
                return userComponentVisibilityMap.get(login).isActiveAren();
        } else {
            if(googleMapComponentVisible.isNotActivePolygon())
                return userComponentVisibilityMap.get(login).isNotActiveAren();
        }

        return false;
    }

    private void renderPolylines(){
        List<Polyline>polylines = googleMapModelOutput.getPolylines();
        polylines.clear();

        for(Polyline polyline : googleMapModel.getPolylines()){
            OverlayUUIDRaw uuidRaw = OverlayUUIDConverter.uuidRaw(polyline.getId());
            if(uuidRaw.getProvider() == Providers.GPS){
                if(filterPolylineGps(uuidRaw))
                    googleMapModelOutput.getPolylines().add(polyline);
            } else if(uuidRaw.getProvider() == Providers.NETWORK) {
                if(uuidRaw.getLocalizationService() == LocalizationServices.NASZ){
                    if(filterPolylineNetwork(uuidRaw))
                        googleMapModelOutput.getPolylines().add(polyline);
                } else if(uuidRaw.getLocalizationService() == LocalizationServices.OBCY) {
                    if(filterPolylineNetwork(uuidRaw))
                        googleMapModelOutput.getPolylines().add(polyline);
                } else {
                    throw new RuntimeException("Nie wspierana usluga dla renderowania polyline " + uuidRaw.getProvider());
                }
            } else {
                throw new RuntimeException("Nie wspierany provider dla renderowania polyline " + uuidRaw.getProvider());
            }
        }
    }

    private boolean filterPolylineGps(OverlayUUIDRaw uuidRaw){
        if(overlayCreateFilter.isCreateGPSPolyline() && googleMapComponentVisible.isGpsPolyline()){
            UserComponentVisibility userComponentVisibility = userComponentVisibilityMap.get(uuidRaw.getLogin());
            return userComponentVisibility.isGPSPolyline();
        }

        return false;
    }

    private boolean filterPolylineNetwork(OverlayUUIDRaw uuidRaw){
        if(uuidRaw.getLocalizationService() == LocalizationServices.NASZ){
            if(overlayCreateFilter.isCreateNetworkNaszPolyline() && googleMapComponentVisible.isNetworkNaszPolyline()){
                UserComponentVisibility userComponentVisibility = userComponentVisibilityMap.get(uuidRaw.getLogin());
                return userComponentVisibility.isNetworkNaszPolyline();
            }
        } else if(uuidRaw.getLocalizationService() == LocalizationServices.OBCY){
            if(overlayCreateFilter.isCreateNetworkObcyPolyline() && googleMapComponentVisible.isNetworkObcyPolyline()) {
                UserComponentVisibility userComponentVisibility = userComponentVisibilityMap.get(uuidRaw.getLogin());
                return userComponentVisibility.isNetworkObcyPolyline();
            }
        } else {
            throw new RuntimeException("Nie wspierany serwis przez filterPolylineNetwork " + uuidRaw.getLocalizationService());
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////    ACTIONS    ///////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void onGoogleMapStateChange(StateChangeEvent event){
        center = GoogleMapModel.center(event.getCenter());
        zoom = event.getZoomLevel();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////    GETTERS SETTERS    /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public UserComponentVisibility UserComponentVisibility(String login){ return this.userComponentVisibilityMap.get(login); }

    public GoogleMapComponentVisible getGoogleMapComponentVisible(){
        return this.googleMapComponentVisible;
    }

    public PolylineFactory getPolylineFactory() {
        return polylineFactory;
    }

    public void setPolylineFactory(PolylineFactory polylineFactory) {
        this.polylineFactory = polylineFactory;
    }

    public PolygonFactory getPolygonFactory() {
        return polygonFactory;
    }

    public void setPolygonFactory(PolygonFactory polygonFactory) {
        this.polygonFactory = polygonFactory;
    }

    public MarkerFactory getMarkerFactory() {
        return markerFactory;
    }

    public void setMarkerFactory(MarkerFactory markerFactory) {
        this.markerFactory = markerFactory;
    }

    public CircleFactory getCircleFactory() {
        return circleFactory;
    }

    public void setCircleFactory(CircleFactory circleFactory) {
        this.circleFactory = circleFactory;
    }

    public LocalizerSession getLocalizerSession() {
        return localizerSession;
    }

    public void setLocalizerSession(LocalizerSession localizerSession) {
        this.localizerSession = localizerSession;
    }

    public GoogleMapModel getGoogleMapModelOutput() {
        return googleMapModelOutput;
    }

    public void setGoogleMapModelOutput(GoogleMapModel googleMapModelOutput) {
        this.googleMapModelOutput = googleMapModelOutput;
    }

    public GoogleMapModel getGoogleMapModel() {
        return googleMapModel;
    }

    public void setGoogleMapModel(GoogleMapModel googleMapModel) {
        this.googleMapModel = googleMapModel;
    }

    public Map<String, UserComponentVisibility> getUserComponentVisibilityMap() {
        return userComponentVisibilityMap;
    }

    public void setUserComponentVisibilityMap(Map<String, UserComponentVisibility> userComponentVisibilityMap) {
        this.userComponentVisibilityMap = userComponentVisibilityMap;
    }

    public void setGoogleMapComponentVisible(GoogleMapComponentVisible googleMapComponentVisible) {
        this.googleMapComponentVisible = googleMapComponentVisible;
    }

    public OverlayCreateFilter getOverlayCreateFilter() {
        return overlayCreateFilter;
    }

    public void setOverlayCreateFilter(OverlayCreateFilter overlayCreateFilter) {
        this.overlayCreateFilter = overlayCreateFilter;
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

    public GoogleMaps getGoogleMapType() {
        return googleMapType;
    }

    public void setGoogleMapType(GoogleMaps googleMapType) {
        this.googleMapType = googleMapType;
    }

    public boolean isDisplayMessageOnSelectOverlay() {
        return displayMessageOnSelectOverlay;
    }

    public void setDisplayMessageOnSelectOverlay(boolean displayMessageOnSelectOverlay) {
        this.displayMessageOnSelectOverlay = displayMessageOnSelectOverlay;
    }

    public Overlay getLastSelectedOverlay() {
        return lastSelectedOverlay;
    }

    public void setLastSelectedOverlay(Overlay lastSelectedOverlay) {
        this.lastSelectedOverlay = lastSelectedOverlay;
    }

}
