package com.pw.localizer.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.pw.localizer.model.enums.LocalizationService;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
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
public class LocationNetwork extends Location implements Serializable{
	@OneToOne(optional = false, orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(nullable = true)
	private CellInfoMobile cellInfoMobile;

	@OneToOne(optional = false, orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(nullable = true)
	private WifiInfo wifiInfo;

	@Enumerated(EnumType.STRING)
	private LocalizationService localizationService;

	@Column(updatable = false)
	private float bearing;

	//@Column(nullable = true)
	private String networkType;
}
