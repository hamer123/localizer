package com.pw.localizer.identyfikator;

/**
 * Created by wereckip on 06.09.2016.
 */

import static org.junit.Assert.*;
import com.pw.localizer.model.enums.Overlays;
import com.pw.localizer.model.enums.Providers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.class)
public class OverlayUUIDRawConverterTest {

    @Test
    public void regexShouldReturnValidRegex(){
        String regex = "MARKER-GPS-any-hamer123-1";
        OverlayUUIDRaw uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.insatnce()
                .overlay(Overlays.MARKER)
                .provider(Providers.GPS)
                .login("hamer123")
                .id(1L)
                .build();

        String resultRegex = OverlayUUIDConverter.regex(uuidRaw);
        assertEquals(regex,resultRegex);
    }
}
