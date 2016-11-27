package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pw.localizer.model.entity.Address;
import com.pw.localizer.model.enums.Provider;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property="type")
@JsonSubTypes({
    @JsonSubTypes.Type(name = "gps", value = LocationGpsDTO.class),
    @JsonSubTypes.Type(name = "network", value = LocationNetworkDTO.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {
    protected Long id;
    @NotNull
    protected double latitude;
    @NotNull
    protected double longitude;
    @NotNull
    protected BasicUserDTO user;
    @NotNull
    protected Date date;
    @NotNull
    protected Provider providerType;
    @NotNull
    protected Address address;
    @NotNull
    protected double accuracy;
}
