package com.pw.localizer.singleton;

import com.pw.localizer.model.entity.*;
import com.pw.localizer.model.enums.LocalizerService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Created by Patryk on 2016-10-14.
 */

@Named
@ApplicationScoped
public class UtilitiesApplicationService {

    public LocalizerService getLocalizerService(AreaEvent areaEvent){
        if(areaEvent != null){
            if(areaEvent.getLocation() instanceof LocationNetwork){
                LocationNetwork locationNetwork = (LocationNetwork) areaEvent.getLocation();
                return locationNetwork.getLocalizerService();
            }
        }
        return null;
    }

}

