package com.pw.localizer.model.entity;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "cellInfoLte")
@XmlAccessorType(XmlAccessType.FIELD)
public class CellInfoLte extends CellInfoMobile{
    private int ci;

    private int mcc;

    private int mnc;

    private int pci;

    private int tac;

	private int timingAdvance;
    
	public int getCi() {
		return ci;
	}

	public void setCi(int ci) {
		this.ci = ci;
	}

	public int getMcc() {
		return mcc;
	}

	public void setMcc(int mcc) {
		this.mcc = mcc;
	}

	public int getMnc() {
		return mnc;
	}

	public void setMnc(int mnc) {
		this.mnc = mnc;
	}

	public int getPci() {
		return pci;
	}

	public void setPci(int pci) {
		this.pci = pci;
	}

	public int getTac() {
		return tac;
	}

	public void setTac(int tac) {
		this.tac = tac;
	}

	public int getTimingAdvance() {
		return timingAdvance;
	}

	public void setTimingAdvance(int timingAdvance) {
		this.timingAdvance = timingAdvance;
	}
}
