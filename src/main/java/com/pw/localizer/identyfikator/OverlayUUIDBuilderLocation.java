package com.pw.localizer.identyfikator;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.enums.Overlays;
import com.pw.localizer.model.enums.Providers;

import static com.pw.localizer.identyfikator.OverlayUUIDRaw.*;

import java.util.regex.Pattern;

/**
 * Created by wereckip on 30.08.2016.
 */

class OverlayUUIDBuilderLocation implements OverlayUUIDBuilder {

    private Location location;
    private Overlays overlay;
    private OverlayUUIDRaw overlayUUIDRaw;

    public OverlayUUIDBuilderLocation(Location location){
        this.location = location;
        buildRaw();
    }

    public OverlayUUIDBuilderLocation(Location location, Overlays overlay){
        this.location = location;
        this.overlay = overlay;
        buildRaw();
    }

    @Override
    public String regex(){
        return null;
    }

    @Override
    public String uuid() {
        return null;
    }

    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public OverlayUUIDRaw uuidRaw() {
        return null;
    }

    private void buildRaw(){
        if(location instanceof LocationGPS){
            LocationGPS locationGPS = (LocationGPS)location;
            this.overlayUUIDRaw = OverlayUUIDRawBuilder.insatnce()
                    .id(locationGPS.getId())
                    .login(locationGPS.getUser().getLogin())
                    .overlay(overlay)
                    .provider(Providers.GPS)
                    .build();
        } else if(location instanceof LocationNetwork) {
            LocationNetwork locationNetwork = (LocationNetwork)location;
            this.overlayUUIDRaw = OverlayUUIDRawBuilder.insatnce()
                    .id(locationNetwork.getId())
                    .login(locationNetwork.getUser().getLogin())
                    .overlay(overlay)
                    .provider(Providers.NETWORK)
                    .localizationService(locationNetwork.getLocalizationServices())
                    .build();
        }
    }
}
