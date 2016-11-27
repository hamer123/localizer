package com.pw.localizer.utilitis;

import com.pw.localizer.model.entity.*;
import com.pw.localizer.model.enums.LocalizationService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Created by Patryk on 2016-10-14.
 */

@Named
@ApplicationScoped
public class UtilitiesApplicationService {

    public LocalizationService getLocalizationService(AreaEvent areaEvent){
        if(areaEvent != null){
            if(areaEvent.getLocation() instanceof LocationNetwork){
                LocationNetwork locationNetwork = (LocationNetwork) areaEvent.getLocation();
                return locationNetwork.getLocalizationService();
            }
        }
        return null;
    }

    public LocalizationService getLocalizationServices(Location location){
        if(location != null) {
            if(location instanceof LocationNetwork) {
                return ((LocationNetwork) location).getLocalizationService();
            }
        }
        return null;
    }
}

