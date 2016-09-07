package com.pw.localizer.identyfikator;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by wereckip on 30.08.2016.
 */

public interface OverlayUUIDBuilder {

    String regex();
    String uuid();
    Pattern pattern();
    OverlayUUIDRaw uuidRaw();
}
