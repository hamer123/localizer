package com.pw.localizer.identyfikator;

import com.pw.localizer.identyfikator.exception.OverlayUUIDExpcetion;
import com.pw.localizer.model.enums.LocalizationServices;
import com.pw.localizer.model.enums.Overlays;
import com.pw.localizer.model.enums.Providers;

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
public final class OverlayUUIDConverter {

    private OverlayUUIDConverter(){}

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

    public static OverlayUUIDRaw uuidRaw(String uuid) {
        String[] parts = splitUUID(uuid);
        return OverlayUUIDRaw.OverlayUUIDRawBuilder.insatnce()
                .overlay(parts[0] == "any" ? null : Overlays.valueOf(parts[0]))
                .provider(parts[1] == "any" ? null : Providers.valueOf(parts[1]))
                .localizationService(parts[2] == "any" ? null : LocalizationServices.valueOf(parts[2]))
                .login(parts[3] == "any" ? null : parts[3])
                .id(parts[4] == "any" ? null : Long.valueOf(parts[4]))
                .build();
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

    public static Overlays extractOverlay(String uuid){
        String[] parts = splitUUID(uuid);
        return parts[0] == "any" ? null : Overlays.valueOf(parts[0]);
    }

    public static Providers extractProvider(String uuid){
        String[] parts = splitUUID(uuid);
        return parts[1] == "any" ? null : Providers.valueOf(parts[1]);
    }

    public static LocalizationServices extractService(String uuid){
        String[] parts = splitUUID(uuid);
        return parts[2] == "any" ? null : LocalizationServices.valueOf(parts[2]);
    }

    public static String extractLogin(String uuid){
        String[] parts = splitUUID(uuid);
        return parts[3] == "any" ? null : parts[3];
    }

    public static Long extractId(String uuid){
        String[] parts = splitUUID(uuid);
        return parts[4] == "any" ? null : Long.valueOf(parts[4]);
    }

    private static String[] splitUUID(String uuid){
        String[] parts = uuid.split("-");
        if(!(parts.length == 5)) throw new OverlayUUIDExpcetion("Nie poprawny format uuid");
        return parts;
    }
}
