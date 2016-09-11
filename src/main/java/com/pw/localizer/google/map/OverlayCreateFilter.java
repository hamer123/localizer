package com.pw.localizer.google.map;

/**
 * Created by wereckip on 08.09.2016.
 */

public class OverlayCreateFilter {
    private boolean createGPSPolyline;
    private boolean createNetworkNaszPolyline;
    private boolean createNetworkObcyPolyline;
    private boolean createActivePolygon;
    private boolean createNotActivePolygon;

    public boolean isCreateGPSPolyline() {
        return createGPSPolyline;
    }

    public void setCreateGPSPolyline(boolean createGPSPolyline) {
        this.createGPSPolyline = createGPSPolyline;
    }

    public boolean isCreateNetworkNaszPolyline() {
        return createNetworkNaszPolyline;
    }

    public void setCreateNetworkNaszPolyline(boolean createNetworkNaszPolyline) {
        this.createNetworkNaszPolyline = createNetworkNaszPolyline;
    }

    public boolean isCreateNetworkObcyPolyline() {
        return createNetworkObcyPolyline;
    }

    public void setCreateNetworkObcyPolyline(boolean createNetworkObcyPolyline) {
        this.createNetworkObcyPolyline = createNetworkObcyPolyline;
    }

    public boolean isCreateActivePolygon() {
        return createActivePolygon;
    }

    public void setCreateActivePolygon(boolean createActivePolygon) {
        this.createActivePolygon = createActivePolygon;
    }

    public boolean isCreateNotActivePolygon() {
        return createNotActivePolygon;
    }

    public void setCreateNotActivePolygon(boolean createNotActivePolygon) {
        this.createNotActivePolygon = createNotActivePolygon;
    }
}
