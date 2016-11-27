package com.pw.localizer.utilitis;

import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.enums.AreaFollow;

public class AreaEventMessageBuilder {
    private static final String ENTRANCE_AREA_BY_TARGET = " wszedł do obszaru śledzenia '%s' ";
    private static final String LOWERING_AREA_BY_TARGET = " opuścił obszar śledzenia '%s' ";

    private AreaEventMessageBuilder () {}

    public static String create(Area area, Location location) {
        StringBuilder builder = new StringBuilder();
        builder.append("Target ")
                .append(area.getTarget().getLogin())
                .append(eventCasePart(area))
                .append(" dnia ")
                .append(location.getDate())
                .append(" [ usługa lokalizacj ")
                .append(locationProviderPart(location))
                .append(" ]");
        return builder.toString();
    }

    private static String eventCasePart(Area area) {
        if(area.getAreaFollowType() == AreaFollow.INSIDE) {
            return String.format(LOWERING_AREA_BY_TARGET, area.getName());
        } else {
            return String.format(ENTRANCE_AREA_BY_TARGET, area.getName());
        }
    }

    private static String locationProviderPart(Location location) {
        String provider = location.getProviderType().toString();
        if(location instanceof LocationNetwork) {
            LocationNetwork locationNetwork = (LocationNetwork)location;
            provider += " ";
            provider += locationNetwork.getLocalizationService();
        }
        return provider;
    }
}

