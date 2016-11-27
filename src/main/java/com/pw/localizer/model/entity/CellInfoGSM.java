package com.pw.localizer.model.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class CellInfoGSM extends CellInfoMobile{
    private int cid;

    private int lac;

    private int mcc;

    private int mnc;

    private int psc;
}
