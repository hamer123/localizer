package com.pw.localizer.identyfikator;

import java.util.regex.Pattern;

/**
 * Created by wereckip on 06.09.2016.
 */

/**
 * Kolejnosc i wzor uuid
 *
 * Overlay-Provider-Service-Login-Id
 *
 * Overlay (Marker,Circle,Polygon,Polyline)
 * Provider (GPS,Network)
 * Service (Nasz,Obcy)
 * Login (Login uzytkownika)
 * Id encji (Location,Area)
 *
 */
class OverlayUUIDRawConverter {

    private OverlayUUIDRawConverter(){}

    public static String regex(OverlayUUIDRaw uuidRaw){
        StringBuilder builderRegex = new StringBuilder();

        builderRegex.append(uuidRaw.getOverlay() == null ? "*" : uuidRaw.getOverlay());
        builderRegex.append("-");
        builderRegex.append(uuidRaw.getProvider() == null ? "*" : uuidRaw.getProvider());
        builderRegex.append("-");
        builderRegex.append(uuidRaw.getLocalizationService() == null ? "*" : uuidRaw.getLocalizationService());
        builderRegex.append("-");
        builderRegex.append(uuidRaw.getLogin() == null ? "*" : uuidRaw.getLogin());
        builderRegex.append("-");
        builderRegex.append(uuidRaw.getId() == null ? "*" : uuidRaw.getId());

        return builderRegex.toString();
    }

    public static String uuid(OverlayUUIDRaw uuidRaw){
        StringBuilder builderUUID = new StringBuilder();

        builderUUID.append(uuidRaw.getOverlay() == null ? "any" : uuidRaw.getOverlay());
        builderUUID.append("-");
        builderUUID.append(uuidRaw.getProvider() == null ? "any" : uuidRaw.getProvider());
        builderUUID.append("-");
        builderUUID.append(uuidRaw.getLocalizationService() == null ? "any" : uuidRaw.getLocalizationService());
        builderUUID.append("-");
        builderUUID.append(uuidRaw.getLogin() == null ? "any" : uuidRaw.getLogin());
        builderUUID.append("-");
        builderUUID.append(uuidRaw.getId() == null ? "any" : uuidRaw.getId());

        return builderUUID.toString();
    }

    public static Pattern pattern(OverlayUUIDRaw uuidRaw){
        return Pattern.compile(regex(uuidRaw));
    }
}
