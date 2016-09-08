package com.pw.localizer.google.map;

import com.pw.localizer.identyfikator.OverlayUUIDConverter;
import com.pw.localizer.identyfikator.OverlayUUIDRaw;
import com.pw.localizer.jsf.utilitis.CircleBuilder;
import com.pw.localizer.jsf.utilitis.MarkerBuilder;
import com.pw.localizer.jsf.utilitis.PolygonBuilder;
import com.pw.localizer.jsf.utilitis.PolylineBuilder;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.entity.UserSetting;
import com.pw.localizer.model.enums.GoogleMaps;
import com.pw.localizer.model.enums.LocalizationServices;
import com.pw.localizer.model.enums.Overlays;
import com.pw.localizer.model.enums.Providers;
import com.pw.localizer.model.google.map.GoogleMapComponentVisible;
import com.pw.localizer.model.google.map.GoogleMapModel;
import com.pw.localizer.model.session.LocalizerSession;
import org.primefaces.model.map.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
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

@Named
@Dependent
public class UserGoogleMapController implements Serializable{
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

    @Inject
    private LocalizerSession localizerSession;

    @PostConstruct
    private void postConstruct(){
        if(this.localizerSession.getUser() == null){

        } else {
            UserSetting userSetting = this.localizerSession.getUser().getUserSetting();
            this.zoom = userSetting.getgMapZoom();
            this.center = GoogleMapModel.center(userSetting.getDefaultLatitude(), userSetting.getDefaultLongtitude());
        }
    }

    public void add(User user){
        addCircle(user);
        addMarker(user);
        addPolygon(user);
        addPolyline(user);
        userComponentVisibilityMap.put(user.getLogin(),new UserComponentVisibility());
    }



    public void remove(String login){
        removeCircle(login);
        removeMarker(login);
        removePolygon(login);
        removePolyline(login);
        userComponentVisibilityMap.remove(login);
    }

    public void update(User user){

    }

    public void update(User user, Overlays overlays){

    }

    public void render(){
        //TODO
    }


    public UserComponentVisibility UserComponentVisibility(String login){
        return this.userComponentVisibilityMap.get(login);
    }

    public GoogleMapComponentVisible getGoogleMapComponentVisible(){
        return this.googleMapComponentVisible;
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
        Marker marker = MarkerBuilder.createMarker(location);
        markers.add(marker);
        if(filterGpsMarker(marker, OverlayUUIDConverter.uuidRaw(marker.getId())))
            googleMapModelOutput.getMarkers().add(marker);
    }

    private void addNetworkMarker(Location location){
        List<Marker>markers = googleMapModel.getMarkers();
        Marker marker = MarkerBuilder.createMarker(location);
        markers.add(marker);
        if(filterNetworkMarker(marker,OverlayUUIDConverter.uuidRaw(marker.getId())))
            googleMapModelOutput.getMarkers().add(marker);
    }

    private void addCircle(User user){
        List<Circle>circles = googleMapModel.getCircles();
        Location location = user.getLastLocationGPS();
        if(location != null)
            circles.add(CircleBuilder.createCircle(location));

        location = user.getLastLocationNetworkNaszaUsluga();
        if(location != null)
            circles.add(CircleBuilder.createCircle(location));

        location = user.getLastLocationNetworObcaUsluga();
        if(location != null)
            circles.add(CircleBuilder.createCircle(location));
    }

    private void addCircleGps(Location location){
        List<Circle>circles = googleMapModel.getCircles();
        Circle circle = CircleBuilder.createCircle(location);
        circles.add(circle);
    }

    private void addPolygon(User user){
        List<Polygon>polygons = googleMapModel.getPolygons();
        List<Area>areas = user.getAreas();
        polygons.addAll(PolygonBuilder.createPolygons(areas));
    }

    private void addPolyline(User user){
        List<Polyline>polylines = googleMapModel.getPolylines();
        Location location = user.getLastLocationGPS();
        if(location != null && overlayCreateFilter.isCreateGPSPolyline())
            polylines.add(PolylineBuilder.create(location));

        location = user.getLastLocationNetworkNaszaUsluga();
        if(location != null && overlayCreateFilter.isCreateNetworkNaszPolyline())
            polylines.add(PolylineBuilder.create(location));

        location = user.getLastLocationNetworObcaUsluga();
        if(location != null && overlayCreateFilter.isCreateNetworkObcyPolyline())
            polylines.add(PolylineBuilder.create(location));
    }

    private void removeMarker(String login){
        Iterator<Marker>markerIterator = googleMapModel.getMarkers().iterator();
        while(markerIterator.hasNext()){
            Marker marker = markerIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(marker.getId());
            if(login.equals(extractLogin))
                markerIterator.remove();
        }
    }

    private void removeCircle(String login){
        Iterator<Circle>circleIterator = googleMapModel.getCircles().iterator();
        while(circleIterator.hasNext()){
            Circle circle = circleIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(circle.getId());
            if(login.equals(extractLogin))
                circleIterator.remove();
        }
    }

    private void removePolygon(String login){
        Iterator<Polygon>polygonIterator = googleMapModel.getPolygons().iterator();
        while(polygonIterator.hasNext()){
            Polygon polygon = polygonIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(polygon.getId());
            if(login.equals(extractLogin))
                polygonIterator.remove();
        }
    }

    private void removePolyline(String login){
        Iterator<Polyline>polylineIterator = googleMapModel.getPolylines().iterator();
        while(polylineIterator.hasNext()){
            Polyline polyline = polylineIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(polyline.getId());
            if(login.equals(extractLogin))
                polylineIterator.remove();
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

    }

    private void renderPolylines(){

    }
}
