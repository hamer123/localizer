package com.pw.localizer.identyfikator;

/**
 * Created by wereckip on 06.09.2016.
 */

import static org.junit.Assert.*;

import com.pw.localizer.identyfikator.exception.OverlayUUIDExpcetion;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.enums.OverlayType;
import com.pw.localizer.model.enums.Provider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.regex.Pattern;

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

public class OverlayUUIDRawConverterTest {

    @Test
    public void uuidRegexShouldMatchResultTest(){
        String regex = "MARKER-GPS-.*-hamer123-1";
        OverlayUUIDRaw uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.instance()
                .overlay(OverlayType.MARKER)
                .provider(Provider.GPS)
                .login("hamer123")
                .id(1L)
                .build();
        String resultRegex = OverlayUUIDConverter.regex(uuidRaw);
        assertEquals(regex,resultRegex);
    }

    @Test
    public void uuidShouldMatchResultTest(){
        String uuid = "MARKER-GPS-any-hamer123-1";
        OverlayUUIDRaw uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.instance()
                .overlay(OverlayType.MARKER)
                .provider(Provider.GPS)
                .login("hamer123")
                .id(1L)
                .build();
        String resultUUID = OverlayUUIDConverter.uuid(uuidRaw);
        assertEquals(uuid,resultUUID);
    }

    @Test
    public void uuidRegexShouldNotMatch() {
        String regex = "MARKER-GPS-.*-hamer123-1";
        OverlayUUIDRaw uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.instance()
                .id(2L)
                .build();
        boolean result = regex.matches(OverlayUUIDConverter.regex(uuidRaw));
        assertFalse(result);
    }

    @Test
    public void uuidShouldNotMatch() {
        String uuid = "MARKER-GPS-any-hamer123-1";
        OverlayUUIDRaw uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.instance()
                .id(2L)
                .build();
        boolean result = OverlayUUIDUtilitis.matches(uuid, OverlayUUIDConverter.uuid(uuidRaw));
        assertFalse(result);
    }

    @Test
    public void uuidShouldMatchPatternAndPatternShouldMatchRegex() {
        String uuid = "MARKER-GPS-any-hamer123-2";
        LocationGPS location = new LocationGPS();
        location.setId(2L);
        location.setProviderType(Provider.GPS);
        User user = new User();
        user.setLogin("hamer123");
        location.setUser(user);

        OverlayUUIDBuilderLocation overlayUUIDBuilderLocation = new OverlayUUIDBuilderLocation(location, OverlayType.MARKER);
        String regex = overlayUUIDBuilderLocation.regex();
        Pattern pattern = overlayUUIDBuilderLocation.pattern();

        assertTrue(pattern.matcher(uuid).matches());
        assertEquals(pattern.pattern(), regex);
    }

    @Test(expected = OverlayUUIDExpcetion.class)
    public void expectOverlayUUIDException() {
        String uuid = "MARKER-GPS-any-hamer123-2-2";
        OverlayUUIDUtilitis.matches("1-1-1-1-1", uuid);
    }
}
