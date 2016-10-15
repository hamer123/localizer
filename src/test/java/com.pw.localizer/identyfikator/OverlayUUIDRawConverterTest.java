package com.pw.localizer.identyfikator;

/**
 * Created by wereckip on 06.09.2016.
 */

import static org.junit.Assert.*;
import com.pw.localizer.model.enums.OverlayType;
import com.pw.localizer.model.enums.Provider;
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
        OverlayUUIDRaw uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.instance()
                .overlay(OverlayType.MARKER)
                .provider(Provider.GPS)
                .login("hamer123")
                .id(1L)
                .build();

        String resultRegex = OverlayUUIDConverter.regex(uuidRaw);
        assertEquals(regex,resultRegex);
    }
}
