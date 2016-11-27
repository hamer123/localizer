package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLastLocationsDTO {
    private LocationNetworkDTO lastLocationNetworkNasz;
    private LocationNetworkDTO lastLocationNetworkObcy;
    private LocationGpsDTO lastLocationGps;
}
