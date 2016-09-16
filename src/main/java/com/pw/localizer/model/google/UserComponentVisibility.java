package com.pw.localizer.model.google;

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

    private boolean activeAren = true;
    private boolean NotActiveAren = true;

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

    public boolean isActiveAren() {
        return activeAren;
    }

    public void setActiveAren(boolean activeAren) {
        this.activeAren = activeAren;
    }

    public boolean isNotActiveAren() {
        return NotActiveAren;
    }

    public void setNotActiveAren(boolean notActiveAren) {
        this.NotActiveAren = notActiveAren;
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
