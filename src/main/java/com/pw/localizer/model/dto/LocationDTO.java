package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pw.localizer.model.entity.Address;
import com.pw.localizer.model.enums.Provider;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
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
    private Long id;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    private BasicUserDTO user;
    private Date date;
    @NotNull
    private Provider providerType;
    @Valid
    private Address address;
    private double accuracy;
}
