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
    private long userId;
    private LocationNetwork lastLocationNetworkNasz;
    private LocationNetwork lastLocationNetworkObcy;
    private LocationGPS lastLocationGps;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocationNetwork getLastLocationNetworkNasz() {
        return lastLocationNetworkNasz;
    }

    public void setLastLocationNetworkNasz(LocationNetwork lastLocationNetworkNasz) {
        this.lastLocationNetworkNasz = lastLocationNetworkNasz;
    }

    public LocationNetwork getLastLocationNetworkObcy() {
        return lastLocationNetworkObcy;
    }

    public void setLastLocationNetworkObcy(LocationNetwork lastLocationNetworkObcy) {
        this.lastLocationNetworkObcy = lastLocationNetworkObcy;
    }

    public LocationGPS getLastLocationGps() {
        return lastLocationGps;
    }

    public void setLastLocationGps(LocationGPS lastLocationGps) {
        this.lastLocationGps = lastLocationGps;
    }

    public static User convertToEntity(){
        return null;
    }


    public static UserLastLocationsDTO convertToDto(User user){
        UserLastLocationsDTO userLastLocationsDTO = new UserLastLocationsDTO();
        userLastLocationsDTO.setLastLocationGps(user.getLastLocationGPS());
        userLastLocationsDTO.setLastLocationNetworkNasz(user.getLastLocationNetworkNaszaUsluga());
        userLastLocationsDTO.setLastLocationNetworkObcy(user.getLastLocationNetworObcaUsluga());
        return userLastLocationsDTO;
    }
}
