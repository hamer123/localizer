package com.pw.localizer.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class CellIdentityCdma {
    // Network Id 0..65535
    private int mNetworkId;
    // CDMA System Id 0..32767
    private int mSystemId;
    // Base Station Id 0..65535
    private int mBasestationId;
    /**
     * Longitude is a decimal number as specified in 3GPP2 C.S0005-A v6.0.
     * It is represented in units of 0.25 seconds and ranges from -2592000
     * to 2592000, both values inclusive (corresponding to a range of -180
     * to +180 degrees).
     */
    private int mLongitude;
    /**
     * Latitude is a decimal number as specified in 3GPP2 C.S0005-A v6.0.
     * It is represented in units of 0.25 seconds and ranges from -1296000
     * to 1296000, both values inclusive (corresponding to a range of -90
     * to +90 degrees).
     */
    private int mLatitude;
}
