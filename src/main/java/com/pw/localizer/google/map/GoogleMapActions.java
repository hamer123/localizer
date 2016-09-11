package com.pw.localizer.google.map;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;

/**
 * Created by Patryk on 2016-09-11.
 */

public interface GoogleMapActions {
    void onGoogleMapStateChange(StateChangeEvent event);
    void onOverlaySelect(OverlaySelectEvent event);
}
