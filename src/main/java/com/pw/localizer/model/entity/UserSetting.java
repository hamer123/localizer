package com.pw.localizer.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class UserSetting implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private long id;
	@NotNull
	private double defaultLatitude;
	@NotNull
	private double defaultLongitude;
	@NotNull
	private int gMapZoom;
}
