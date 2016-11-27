package com.pw.localizer.controller.google;

import com.pw.localizer.exception.LocationServiceNotSupportedException;
import com.pw.localizer.exception.ProviderNotSupportedException;
import com.pw.localizer.identyfikator.OverlayUUIDConverter;
import com.pw.localizer.identyfikator.OverlayUUIDRaw;
import com.pw.localizer.model.entity.*;
import com.pw.localizer.factory.CircleFactory;
import com.pw.localizer.factory.MarkerFactory;
import com.pw.localizer.factory.PolygonFactory;
import com.pw.localizer.factory.PolylineFactory;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.model.enums.OverlayType;
import com.pw.localizer.model.enums.Provider;
import com.pw.localizer.model.google.GoogleMap;
import com.pw.localizer.model.google.GoogleMapComponentVisible;
import com.pw.localizer.model.google.OverlayCreateFilter;
import com.pw.localizer.model.google.UserComponentVisibility;
import com.pw.localizer.model.session.LocalizerSession;
import com.pw.localizer.singleton.LocalizerProperties;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.*;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * Created by wereckip on 08.09.2016.
 */

@ViewScoped
@Named(value = "userGoogleMap")
@Getter
@Setter
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
    @Inject
    private LocalizerProperties localizerProperties;

    /** Gotowy model po renderingu */
    private GoogleMap googleMapOutput = new GoogleMap();

    /** Zawartosc po utworzeniu */
    private GoogleMap googleMap = new GoogleMap();

    /** Widocznosc komponentow indywidulanie dla uzytkoniwka */
    private Map<String,UserComponentVisibility> userComponentVisibilityMap = new HashMap<>();

    /** Ogolna widocznosc komponentow */
    private GoogleMapComponentVisible googleMapComponentVisible = new GoogleMapComponentVisible();

    /** Ograniczenie tworzenia komponentow */
    private OverlayCreateFilter overlayCreateFilter = new OverlayCreateFilter();

    /** Zoom */
    private int zoom;

    /** Map center */
    private String center;

    /** street visible ? */
    private boolean streetVisible = true;

    /** Google map type */
    private com.pw.localizer.model.enums.GoogleMap googleMapType = com.pw.localizer.model.enums.GoogleMap.TERRAIN;

    /** Display message on overlay select ? */
    private boolean displayMessageOnSelectOverlay = true;

    /** last selected overlay */
    private Overlay lastSelectedOverlay;

    @PostConstruct
    private void postConstruct(){
        this.googleMapType = com.pw.localizer.model.enums.GoogleMap.HYBRID;
        if(this.localizerSession.getUser() == null){
            center = localizerProperties.GoogleMapDefault().DEFAULT_CENTER;
            zoom = localizerProperties.GoogleMapDefault().DEFAULT_ZOOM;
        } else {
            UserSetting userSetting = this.localizerSession.getUser().getUserSetting();
            this.zoom = userSetting.getgMapZoom();
            this.center = GoogleMap.center(userSetting.getDefaultLatitude(), userSetting.getDefaultLongitude());
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

    public void update(User user, OverlayType overlay){
        if(overlay == OverlayType.MARKER){
            removeMarker(user.getLogin());
            addMarker(user);
        } else if(overlay == OverlayType.CIRCLE){
            removeCircle(user.getLogin());
            addCircle(user);
        } else if(overlay == OverlayType.POLYLINE){
            updatePolylines(user);
        } else if(overlay == OverlayType.POLYGON){
            removePolygon(user.getLogin());
            addPolygon(user);
        } else {
            throw new RuntimeException("Not supported overlay " + overlay);
        }
    }

    public void render(){
        renderCircles();
        renderMarkers();
        renderPolygons();
        renderPolylines();
    }

    public void render(OverlayType overlay){
        if(overlay == OverlayType.MARKER){
            renderMarkers();
        } else if(overlay == OverlayType.CIRCLE){
            renderCircles();
        } else if(overlay == OverlayType.POLYGON){
            renderPolygons();
        } else if(overlay == OverlayType.POLYLINE){
            renderPolylines();
        } else {
            throw new RuntimeException("Not supported overlay " + overlay);
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
        List<Marker>markers = googleMap.getMarkers();
        Marker marker = markerFactory.createMarker(location);
        markers.add(marker);
        if(filterGpsMarker(marker, OverlayUUIDConverter.uuidRaw(marker.getId())))
            googleMapOutput.getMarkers().add(marker);
    }

    private void addNetworkMarker(Location location){
        List<Marker>markers = googleMap.getMarkers();
        Marker marker = markerFactory.createMarker(location);
        markers.add(marker);
        if(filterNetworkMarker(marker,OverlayUUIDConverter.uuidRaw(marker.getId())))
            googleMapOutput.getMarkers().add(marker);
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
        List<Circle>circles = googleMap.getCircles();
        Circle circle = circleFactory.createCircle(location);
        circles.add(circle);
        if(filterCircleGps(circle,OverlayUUIDConverter.uuidRaw(circle.getId())))
            googleMapOutput.getCircles().add(circle);
    }

    private void addCircleNetwork(Location location){
        List<Circle>circles = googleMap.getCircles();
        Circle circle = circleFactory.createCircle(location);
        circles.add(circle);
        if(filterCircleNetwork(circle,OverlayUUIDConverter.uuidRaw(circle.getId())))
            googleMapOutput.getCircles().add(circle);
    }

    /** Polygon data is boolean active variable */
    private void addPolygon(User user){
        List<Polygon>polygons = googleMap.getPolygons();
        Set<Area>areas = user.getAreas();
        for(Area area : areas){
            if(overlayCreateFilter.isCreateActivePolygon() && area.isActive()){
                Polygon polygon = polygonFactory.create(area, true);
                polygons.add(polygon);
                if(filterPolygon(polygon,OverlayUUIDConverter.uuidRaw(polygon.getId())))
                    googleMapOutput.getPolygons().add(polygon);
            } else if(overlayCreateFilter.isCreateNotActivePolygon() && !area.isActive()){
                Polygon polygon = polygonFactory.create(area, false);
                polygons.add(polygon);
                if(filterPolygon(polygon,OverlayUUIDConverter.uuidRaw(polygon.getId())))
                    googleMapOutput.getPolygons().add(polygon);
            }
        }
    }

    private void updatePolylines(User user){
        updatePolylineGps(user);
        updatePolylineNetwork(user);
    }

    private void updatePolylineGps(User user){
        Location location = user.getLastLocationGPS();
        OverlayUUIDRaw uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.instance()
                .login(user.getLogin())
                .provider(Provider.GPS)
                .build();
        updatePolyline(location,uuidRaw);
    }

    private void updatePolylineNetwork(User user){
        Location location = user.getLastLocationNetworkNaszaUsluga();
        OverlayUUIDRaw uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.instance()
                .login(user.getLogin())
                .provider(Provider.NETWORK)
                .localizationService(LocalizationService.NASZ)
                .build();
        updatePolyline(location,uuidRaw);

        location = user.getLastLocationNetworObcaUsluga();
        uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.instance()
                .login(user.getLogin())
                .provider(Provider.NETWORK)
                .localizationService(LocalizationService.OBCY)
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
                if(location.getProviderType() == Provider.GPS){
                    addPolylineGps(location);
                } else if(location.getProviderType() == Provider.NETWORK){
                    addPolylineNetwork(location);
                } else {
                    throw new ProviderNotSupportedException("Not supported provider " + location.getProviderType());
                }
            }
        }
    }

    private Polyline findPolyline(OverlayUUIDRaw uuidRaw){
        for(Polyline polyline : googleMap.getPolylines()){
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
            googleMap.getPolylines().add(polyline);
            if(filterPolylineGps(OverlayUUIDConverter.uuidRaw(polyline.getId()))){
                googleMapOutput.getPolylines().add(polyline);
            }
        }
    }

    private void addPolylineNetwork(Location location){
        List<Polyline>polylines = googleMap.getPolylines();
        LocationNetwork locationNetwork = (LocationNetwork)location;
        if(locationNetwork.getLocalizationService() == LocalizationService.NASZ){
            if(overlayCreateFilter.isCreateNetworkNaszPolyline()){
                Polyline polyline = polylineFactory.create(location,location.getDate());
                polylines.add(polyline);
                if(filterPolylineNetwork(OverlayUUIDConverter.uuidRaw(polyline.getId()))){
                    googleMapOutput.getPolylines().add(polyline);
                }
            }
        } else if(locationNetwork.getLocalizationService() == LocalizationService.OBCY){
            if(overlayCreateFilter.isCreateNetworkObcyPolyline()){
                Polyline polyline = polylineFactory.create(location,location.getDate());
                polylines.add(polyline);
                if(filterPolylineNetwork(OverlayUUIDConverter.uuidRaw(polyline.getId()))){
                    googleMapOutput.getPolylines().add(polyline);
                }
            }
        } else {
            throw new LocationServiceNotSupportedException("Not supported localizer service " + ((LocationNetwork) location).getLocalizationService());
        }
    }

    private void removeMarker(String login){
        Iterator<Marker>markerIterator = googleMap.getMarkers().iterator();
        while(markerIterator.hasNext()){
            Marker marker = markerIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(marker.getId());
            if(login.equals(extractLogin)) {
                markerIterator.remove();
                googleMapOutput.getMarkers().remove(marker);
            }
        }
    }

    private void removeCircle(String login){
        Iterator<Circle>circleIterator = googleMap.getCircles().iterator();
        while(circleIterator.hasNext()){
            Circle circle = circleIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(circle.getId());
            if(login.equals(extractLogin)) {
                circleIterator.remove();
                googleMapOutput.getCircles().remove(circle);
            }
        }
    }

    private void removePolygon(String login){
        Iterator<Polygon>polygonIterator = googleMap.getPolygons().iterator();
        while(polygonIterator.hasNext()){
            Polygon polygon = polygonIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(polygon.getId());
            if(login.equals(extractLogin)) {
                polygonIterator.remove();
                googleMapOutput.getPolygons().remove(polygon);
            }
        }
    }

    private void removePolyline(String login){
        Iterator<Polyline>polylineIterator = googleMap.getPolylines().iterator();
        while(polylineIterator.hasNext()){
            Polyline polyline = polylineIterator.next();
            String extractLogin = OverlayUUIDConverter.extractLogin(polyline.getId());
            if(login.equals(extractLogin)) {
                polylineIterator.remove();
                googleMapOutput.getPolylines().remove(polyline);
            }
        }
    }

    private void renderMarkers(){
        List<Marker>markers = googleMapOutput.getMarkers();
        markers.clear();
        for(Marker marker : googleMap.getMarkers()){
            OverlayUUIDRaw uuidRaw = OverlayUUIDConverter.uuidRaw(marker.getId());
            if(uuidRaw.getProvider() == Provider.GPS){
                if(filterGpsMarker(marker,uuidRaw))
                    markers.add(marker);
            } else if(uuidRaw.getProvider() == Provider.NETWORK){
                if(filterNetworkMarker(marker,uuidRaw))
                    markers.add(marker);
            } else {
                throw new ProviderNotSupportedException("Not supported provider " + uuidRaw.getProvider());
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
        LocalizationService service = uuidRaw.getLocalizationService();
        UserComponentVisibility userComponentVisibility = userComponentVisibilityMap.get(uuidRaw.getLogin());
        if(service == LocalizationService.NASZ){
            if(googleMapComponentVisible.isMarkerNetworkNasz())
                return userComponentVisibility.isNetworkNaszMarker();
        } else if(service == LocalizationService.OBCY){
            if(googleMapComponentVisible.isMarkerNetworkObcy())
                return userComponentVisibility.isNetworkObcyMarker();
        } else {
            throw new LocationServiceNotSupportedException("Not supported localizer service " + uuidRaw.getLocalizationService());
        }
        return false;
    }

    private void renderCircles(){
        List<Circle>circles = googleMapOutput.getCircles();
        circles.clear();

        for(Circle circle : googleMap.getCircles()){
            OverlayUUIDRaw uuidRaw = OverlayUUIDConverter.uuidRaw(circle.getId());
            if(uuidRaw.getProvider() == Provider.GPS){
                if(filterCircleGps(circle,uuidRaw))
                    circles.add(circle);
            } else if(uuidRaw.getProvider() == Provider.NETWORK){
                if(filterCircleNetwork(circle,uuidRaw))
                    circles.add(circle);
            } else {
                throw new ProviderNotSupportedException("Not supported provider " + uuidRaw.getProvider());
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
        LocalizationService service = uuidRaw.getLocalizationService();
        UserComponentVisibility userComponentVisibility = userComponentVisibilityMap.get(uuidRaw.getLogin());
        if(service == LocalizationService.NASZ){
            if(googleMapComponentVisible.isCircleNetworkNasz()){
                return userComponentVisibility.isNetworkNaszCircle();
            }
        } else if(service == LocalizationService.OBCY){
            if(googleMapComponentVisible.isCircleNetworkObcy())
                return userComponentVisibility.isNetworkObcyCircle();
        } else {
            throw new LocationServiceNotSupportedException("Not supported localizer service " + uuidRaw.getLocalizationService());
        }

        return false;
    }

    private void renderPolygons(){
        List<Polygon>polygons = googleMapOutput.getPolygons();
        polygons.clear();

        for(Polygon polygon : googleMap.getPolygons()){
            OverlayUUIDRaw uuidRaw = OverlayUUIDConverter.uuidRaw(polygon.getId());
            if(filterPolygon(polygon,uuidRaw))
                googleMapOutput.getPolygons().add(polygon);
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
        List<Polyline>polylines = googleMapOutput.getPolylines();
        polylines.clear();
        for(Polyline polyline : googleMap.getPolylines()){
            OverlayUUIDRaw uuidRaw = OverlayUUIDConverter.uuidRaw(polyline.getId());
            if(uuidRaw.getProvider() == Provider.GPS){
                if(filterPolylineGps(uuidRaw))
                    googleMapOutput.getPolylines().add(polyline);
            } else if(uuidRaw.getProvider() == Provider.NETWORK) {
                if(uuidRaw.getLocalizationService() == LocalizationService.NASZ){
                    if(filterPolylineNetwork(uuidRaw))
                        googleMapOutput.getPolylines().add(polyline);
                } else if(uuidRaw.getLocalizationService() == LocalizationService.OBCY) {
                    if(filterPolylineNetwork(uuidRaw))
                        googleMapOutput.getPolylines().add(polyline);
                } else {
                    throw new ProviderNotSupportedException("Not supported provider " + uuidRaw.getProvider());
                }
            } else {
                throw new ProviderNotSupportedException("Not supported provider " + uuidRaw.getProvider());
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
        if(uuidRaw.getLocalizationService() == LocalizationService.NASZ){
            if(overlayCreateFilter.isCreateNetworkNaszPolyline() && googleMapComponentVisible.isNetworkNaszPolyline()){
                UserComponentVisibility userComponentVisibility = userComponentVisibilityMap.get(uuidRaw.getLogin());
                return userComponentVisibility.isNetworkNaszPolyline();
            }
        } else if(uuidRaw.getLocalizationService() == LocalizationService.OBCY){
            if(overlayCreateFilter.isCreateNetworkObcyPolyline() && googleMapComponentVisible.isNetworkObcyPolyline()) {
                UserComponentVisibility userComponentVisibility = userComponentVisibilityMap.get(uuidRaw.getLogin());
                return userComponentVisibility.isNetworkObcyPolyline();
            }
        } else {
            throw new LocationServiceNotSupportedException("Not supported localizer service " + uuidRaw.getLocalizationService());
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////    ACTIONS    ///////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void onGoogleMapStateChange(StateChangeEvent event){
        center = GoogleMap.center(event.getCenter());
        zoom = event.getZoomLevel();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////    GETTERS SETTERS    /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
