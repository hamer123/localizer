package com.pw.localizer.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

@Getter
@Setter
@Entity
public class SignalStrengthCdma extends SignalStrength {

    private int cdmaDbm;   // This value is the RSSI value
    private int cdmaEcio;  // This value is the Ec/Io
    private int cdmaLevel;

    private int evdoDbm;   // This value is the EVDO RSSI value
    private int evdoEcio;  // This value is the EVDO Ec/Io
    private int evdoSnr;   // Valid values are 0-8.  8 is the highest signal to noise ratio
    private int evdoLevel;

}
