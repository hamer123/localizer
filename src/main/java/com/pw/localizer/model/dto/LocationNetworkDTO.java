package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pw.localizer.model.entity.CellInfoMobile;
import com.pw.localizer.model.entity.WifiInfo;
import com.pw.localizer.model.enums.LocalizationService;

import javax.validation.constraints.NotNull;

/**
 * Created by Patryk on 2016-10-02.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationNetworkDTO extends LocationDTO {
    @NotNull
    private CellInfoMobile cellInfoMobile;
    @NotNull
    private WifiInfo wifiInfo;
    @NotNull
    private LocalizationService localizationService;

    public CellInfoMobile getCellInfoMobile() {
        return cellInfoMobile;
    }

    public void setCellInfoMobile(CellInfoMobile cellInfoMobile) {
        this.cellInfoMobile = cellInfoMobile;
    }

    public WifiInfo getWifiInfo() {
        return wifiInfo;
    }

    public void setWifiInfo(WifiInfo wifiInfo) {
        this.wifiInfo = wifiInfo;
    }

    public LocalizationService getLocalizationService() {
        return localizationService;
    }

    public void setLocalizationService(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }
}
