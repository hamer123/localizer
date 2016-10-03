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
        locationNetwork = DTOUtilitis.convertHibernateProxyToNull(locationNetwork);
        LocationNetworkDTO locationNetworkDTO = new LocationNetworkDTO();
        locationNetworkDTO.cellInfoMobile = locationNetwork.getCellInfoMobile();
        locationNetworkDTO.wifiInfo = locationNetwork.getWifiInfo();
        locationNetworkDTO.localizerService = locationNetwork.getLocalizerService();
        locationNetworkDTO.user = BasicUserDTO.convertToBasicUserDTO(locationNetwork.getUser());
        locationNetworkDTO.id = locationNetwork.getId();
        locationNetworkDTO.latitude = locationNetwork.getLatitude();
        locationNetworkDTO.longitude = locationNetwork.getLongitude();
        locationNetworkDTO.date = locationNetwork.getDate();
        locationNetworkDTO.providerType = locationNetwork.getProviderType();
        locationNetworkDTO.address = locationNetwork.getAddress();
        locationNetworkDTO.accuracy = locationNetwork.getAccuracy();
        return locationNetworkDTO;
    }

    public static LocationNetwork convertToLocationNetwork(LocationNetworkDTO locationNetworkDTO){
        LocationNetwork locationNetwork = new LocationNetwork();
        return locationNetwork;
    }

}
