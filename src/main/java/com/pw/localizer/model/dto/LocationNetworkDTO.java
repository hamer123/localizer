package com.pw.localizer.model.dto;

import com.pw.localizer.model.entity.CellInfoMobile;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.entity.WifiInfo;
import com.pw.localizer.model.enums.LocalizerService;

/**
 * Created by Patryk on 2016-10-02.
 */
public class LocationNetworkDTO extends LocationDTO {
    private CellInfoMobile cellInfoMobile;
    private WifiInfo wifiInfo;
    private LocalizerService localizerService;

    public static LocationNetworkDTO convertToLocationNetworkDTO(LocationNetwork locationNetwork){
        return null;
    }
}
