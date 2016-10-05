package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pw.localizer.model.entity.Address;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.enums.Provider;
import java.util.Date;

/**
 * Created by Patryk on 2016-10-02.
 */

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property="type")
@JsonSubTypes({
    @JsonSubTypes.Type(name = "gps", value = LocationGPSDTO.class),
    @JsonSubTypes.Type(name = "network", value = LocationNetworkDTO.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {
    protected Long id;
    protected double latitude;
    protected double longitude;
    protected BasicUserDTO user;
    protected Date date;
    protected Provider providerType;
    protected Address address;
    protected double accuracy;

    public static LocationDTO converToLocationDTO(Location location){
        LocationDTO locationDTO = new LocationDTO();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public BasicUserDTO getUser() {
        return user;
    }

    public void setUser(BasicUserDTO user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Provider getProviderType() {
        return providerType;
    }

    public void setProviderType(Provider providerType) {
        this.providerType = providerType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}