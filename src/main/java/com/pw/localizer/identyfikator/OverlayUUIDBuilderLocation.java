package com.pw.localizer.identyfikator;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.enums.OverlayType;
import com.pw.localizer.model.enums.Provider;

import static com.pw.localizer.identyfikator.OverlayUUIDRaw.*;

import java.util.regex.Pattern;

/**
 * Created by wereckip on 30.08.2016.
 */

public class OverlayUUIDBuilderLocation implements OverlayUUIDBuilder {

    private Location location;
    private OverlayType overlay;
    private OverlayUUIDRaw overlayUUIDRaw;

    public OverlayUUIDBuilderLocation(Location location){
        this.location = location;
        buildRaw();
    }

    public OverlayUUIDBuilderLocation(Location location, OverlayType overlay){
        this.location = location;
        this.overlay = overlay;
        buildRaw();
    }

    @Override
    public String regex(){
        return OverlayUUIDConverter.regex(overlayUUIDRaw);
    }

    @Override
    public String uuid() {
        return OverlayUUIDConverter.uuid(overlayUUIDRaw);
    }

    @Override
    public Pattern pattern() {
        return OverlayUUIDConverter.pattern(overlayUUIDRaw);
    }

    @Override
    public OverlayUUIDRaw uuidRaw() {
        return null;
    }

    private void buildRaw(){
        if(location instanceof LocationGPS){
            LocationGPS locationGPS = (LocationGPS)location;
            this.overlayUUIDRaw = OverlayUUIDRawBuilder.instance()
                    .id(locationGPS.getId())
                    .login(locationGPS.getUser().getLogin())
                    .overlay(overlay)
                    .provider(Provider.GPS)
                    .build();
        } else if(location instanceof LocationNetwork) {
            LocationNetwork locationNetwork = (LocationNetwork)location;
            this.overlayUUIDRaw = OverlayUUIDRawBuilder.instance()
                    .id(locationNetwork.getId())
                    .login(locationNetwork.getUser().getLogin())
                    .overlay(overlay)
                    .provider(Provider.NETWORK)
                    .localizationService(locationNetwork.getLocalizationService())
                    .build();
        }
    }
}
