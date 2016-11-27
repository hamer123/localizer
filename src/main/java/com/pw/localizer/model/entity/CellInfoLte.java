package com.pw.localizer.model.entity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class CellInfoLte extends CellInfoMobile{
    private int ci;

    private int mcc;

    private int mnc;

    private int pci;

    private int tac;

	private int timingAdvance;
}
