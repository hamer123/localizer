package com.pw.localizer.identyfikator;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.enums.Overlays;

/**
 * Created by wereckip on 30.08.2016.
 */
public class OverlayUUIDFactory {

    public static OverlayUUIDBuilderLocation builder(Location location){
        return new OverlayUUIDBuilderLocation(location);
    }

    public static OverlayUUIDBuilderLocation builder(Location location, Overlays overlay){
        return new OverlayUUIDBuilderLocation(location,overlay);
    }
}
