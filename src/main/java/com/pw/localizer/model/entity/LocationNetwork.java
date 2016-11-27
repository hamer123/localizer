package com.pw.localizer.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.pw.localizer.model.enums.LocalizationService;

import java.io.Serializable;

@NamedQueries(value = {
		@NamedQuery(name = "findByUserLoginAndDateFromAndDateToAndServiceEqualsNaszOrderByDateDesc",
				   query = "SELECT l FROM LocationNetwork l WHERE l.user.login =:login AND "
				   		 + "l.date > :from AND l.date < :to AND "
				   		 + "l.localizationService = com.pw.localizer.model.enums.LocalizationService.NASZ "
				   		 + "ORDER BY l.date DESC"),
		@NamedQuery(name = "findByUserLoginAndDateFromAndDateToAndServiceEqualsObcyOrderByDateDesc",
				   query = "SELECT l FROM LocationNetwork l WHERE l.user.login =:login AND "
						 + "l.date > :from AND l.date < :to AND "
						 + "l.localizationService = com.pw.localizer.model.enums.LocalizationService.OBCY "
						 + "ORDER BY l.date DESC")
})
@Entity
public class LocationNetwork extends Location implements Serializable{
	@NotNull
	@OneToOne( orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private CellInfoMobile cellInfoMobile;

	@NotNull
	@OneToOne(orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private WifiInfo wifiInfo;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(updatable = false)
	private LocalizationService localizationService;

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

	public LocalizationService getLocalizationService() {
		return localizationService;
	}

	public void setLocalizationService(LocalizationService localizationService) {
		this.localizationService = localizationService;
	}

}
