package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.entity.User;

/**
 * Created by Patryk on 2016-09-24.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLastLocationsDTO {
    private LocationNetworkDTO lastLocationNetworkNasz;
    private LocationNetworkDTO lastLocationNetworkObcy;
    private LocationGPSDTO lastLocationGps;

    public LocationNetworkDTO getLastLocationNetworkNasz() {
        return lastLocationNetworkNasz;
    }

    public void setLastLocationNetworkNasz(LocationNetworkDTO lastLocationNetworkNasz) {
        this.lastLocationNetworkNasz = lastLocationNetworkNasz;
    }

    public LocationNetworkDTO getLastLocationNetworkObcy() {
        return lastLocationNetworkObcy;
    }

    public void setLastLocationNetworkObcy(LocationNetworkDTO lastLocationNetworkObcy) {
        this.lastLocationNetworkObcy = lastLocationNetworkObcy;
    }

    public LocationGPSDTO getLastLocationGps() {
        return lastLocationGps;
    }

    public void setLastLocationGps(LocationGPSDTO lastLocationGps) {
        this.lastLocationGps = lastLocationGps;
    }
}
