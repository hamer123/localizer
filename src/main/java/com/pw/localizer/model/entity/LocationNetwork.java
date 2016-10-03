package com.pw.localizer.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import com.pw.localizer.model.enums.LocalizerService;

import java.io.Serializable;

@NamedQueries(value = {
		@NamedQuery(name = "findByUserLoginAndDateYoungerThanAndOlderThanAndServiceEqualsNaszOrderByDateDesc",
				   query = "SELECT l FROM LocationNetwork l WHERE l.user.login =:login AND "
				   		 + "l.date > :older AND l.date < :younger AND "
				   		 + "l.localizerService = com.pw.localizer.model.enums.LocalizerService.NASZ "
				   		 + "ORDER BY l.date DESC"),
		@NamedQuery(name = "findByUserLoginAndDateYoungerThanAndOlderThanAndServiceEqualsObcyOrderByDateDesc",
				   query = "SELECT l FROM LocationNetwork l WHERE l.user.login =:login AND "
						 + "l.date > :older AND l.date < :younger AND "
						 + "l.localizerService = com.pw.localizer.model.enums.LocalizerService.OBCY "
						 + "ORDER BY l.date DESC")
})
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LocationNetwork extends Location implements Serializable{
	@OneToOne( orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@XmlElementRefs(value = {
			@XmlElementRef(name = "cellInfoLte", type = CellInfoLte.class),
			@XmlElementRef(name = "cellInfoGSM", type = CellInfoGSM.class)
	})
	private CellInfoMobile cellInfoMobile;
	
	@OneToOne(orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private WifiInfo wifiInfo;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(updatable = false)
	private LocalizerService localizerService;

	public CellInfoMobile getCellInfoMobile() {
		return cellInfoMobile;
	}

	public void setCellInfoMobile(CellInfoMobile cellInfoMobile) {
		this.cellInfoMobile = cellInfoMobile;
	}

	public WifiInfo getWifiInfo() {
		return wifiInfo;
	}

	public void setWifiInfo(WifiInfo infoWifi) {
		this.wifiInfo = infoWifi;
	}

	public LocalizerService getLocalizerService() {
		return localizerService;
	}

	public void setLocalizerService(LocalizerService localizerService) {
		this.localizerService = localizerService;
	}

}
