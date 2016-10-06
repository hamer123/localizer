package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pw.localizer.model.entity.CellInfoMobile;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.entity.WifiInfo;
import com.pw.localizer.model.enums.LocalizerService;

/**
 * Created by Patryk on 2016-10-02.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationNetworkDTO extends LocationDTO {
    private CellInfoMobile cellInfoMobile;
    private WifiInfo wifiInfo;
    private LocalizerService localizerService;

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

    public LocalizerService getLocalizerService() {
        return localizerService;
    }

    public void setLocalizerService(LocalizerService localizerService) {
        this.localizerService = localizerService;
    }
}
