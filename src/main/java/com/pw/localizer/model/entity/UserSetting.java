package com.pw.localizer.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
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

	public double getDefaultLatitude() {
		return defaultLatitude;
	}
	public void setDefaultLatitude(double defaultLatitude) {
		this.defaultLatitude = defaultLatitude;
	}
	public double getDefaultLongitude() {
		return defaultLongitude;
	}
	public void setDefaultLongitude(double defaultLongitude) {
		this.defaultLongitude = defaultLongitude;
	}
	public int getgMapZoom() {
		return gMapZoom;
	}
	public void setgMapZoom(int gMapZoom) {
		this.gMapZoom = gMapZoom;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
