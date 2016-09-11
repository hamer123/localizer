package com.pw.localizer.google.map;

/**
 * Created by wereckip on 08.09.2016.
 */
public class UserComponentVisibility {
    private boolean GPSMarker = true;
    private boolean networkNaszMarker = true;
    private boolean networkObcyMarker = true;

    private boolean GPSCircle = true;
    private boolean networkNaszCircle = true;
    private boolean networkObcyCircle = true;

    private boolean activeAreny = true;
    private boolean NotActiveAreny;

    private boolean GPSPolyline;
    private boolean networkNaszPolyline;
    private boolean networkObcyPolyline;

    public boolean isGPSMarker() {
        return GPSMarker;
    }

    public void setGPSMarker(boolean GPSMarker) {
        this.GPSMarker = GPSMarker;
    }

    public boolean isNetworkNaszMarker() {
        return networkNaszMarker;
    }

    public void setNetworkNaszMarker(boolean networkNaszMarker) {
        this.networkNaszMarker = networkNaszMarker;
    }

    public boolean isNetworkObcyMarker() {
        return networkObcyMarker;
    }

    public void setNetworkObcyMarker(boolean networkObcyMarker) {
        this.networkObcyMarker = networkObcyMarker;
    }

    public boolean isGPSCircle() {
        return GPSCircle;
    }

    public void setGPSCircle(boolean GPSCircle) {
        this.GPSCircle = GPSCircle;
    }

    public boolean isNetworkNaszCircle() {
        return networkNaszCircle;
    }

    public void setNetworkNaszCircle(boolean networkNaszCircle) {
        this.networkNaszCircle = networkNaszCircle;
    }

    public boolean isNetworkObcyCircle() {
        return networkObcyCircle;
    }

    public void setNetworkObcyCircle(boolean networkObcyCircle) {
        this.networkObcyCircle = networkObcyCircle;
    }

    public boolean isActiveAreny() {
        return activeAreny;
    }

    public void setActiveAreny(boolean activeAreny) {
        this.activeAreny = activeAreny;
    }

    public boolean isNotActiveAreny() {
        return NotActiveAreny;
    }

    public void setNotActiveAreny(boolean notActiveAreny) {
        this.NotActiveAreny = notActiveAreny;
    }

    public boolean isGPSPolyline() {
        return GPSPolyline;
    }

    public void setGPSPolyline(boolean GPSPolyline) {
        this.GPSPolyline = GPSPolyline;
    }

    public boolean isNetworkNaszPolyline() {
        return networkNaszPolyline;
    }

    public void setNetworkNaszPolyline(boolean networkNaszPolyline) {
        this.networkNaszPolyline = networkNaszPolyline;
    }

    public boolean isNetworkObcyPolyline() {
        return networkObcyPolyline;
    }

    public void setNetworkObcyPolyline(boolean networkObcyPolyline) {
        this.networkObcyPolyline = networkObcyPolyline;
    }
}
