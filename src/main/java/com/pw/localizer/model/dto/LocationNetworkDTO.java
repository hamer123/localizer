package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pw.localizer.model.entity.CellInfoMobile;
import com.pw.localizer.model.entity.WifiInfo;
import com.pw.localizer.model.enums.LocalizationService;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationNetworkDTO extends LocationDTO {
    @Valid
    private CellInfoMobile cellInfoMobile;
    @Valid
    private WifiInfo wifiInfo;
    @NotNull
    private LocalizationService localizationService;
}
