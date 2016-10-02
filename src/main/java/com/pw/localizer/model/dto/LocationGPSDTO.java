package com.pw.localizer.model.dto;

import com.pw.localizer.model.entity.LocationGPS;

/**
 * Created by Patryk on 2016-10-02.
 */

public class LocationGPSDTO extends LocationDTO {

    public static LocationGPSDTO convertToLocationGpsDTO(LocationGPS location){
        LocationGPSDTO locationDTO = new LocationGPSDTO();
        locationDTO.user = BasicUserDTO.convertToBasicUserDTO(location.getUser());
        locationDTO.id = location.getId();
        locationDTO.latitude = location.getLatitude();
        locationDTO.longitude = location.getLongitude();
        locationDTO.date = location.getDate();
        locationDTO.providerType = location.getProviderType();
        locationDTO.address = location.getAddress();
        locationDTO.accuracy = location.getAccuracy();
        return locationDTO;
    }

    public static LocationGPS convertToLocationGps(LocationGPSDTO locationGPSDTO){
        LocationGPS locationGPS = new LocationGPS();
        locationGPS.setUser(BasicUserDTO.convertToUser(locationGPSDTO.getUser()));
        locationGPS.setId(locationGPSDTO.getId());
        locationGPS.setLatitude(locationGPSDTO.getLatitude());
        locationGPS.setLongitude(locationGPSDTO.getLongitude());
        locationGPS.setDate(locationGPSDTO.getDate());
        locationGPS.setProviderType(locationGPSDTO.getProviderType());
        locationGPS.setAddress(locationGPSDTO.getAddress());
        locationGPS.setAccuracy(locationGPSDTO.getAccuracy());
        return locationGPS;
    }
}
