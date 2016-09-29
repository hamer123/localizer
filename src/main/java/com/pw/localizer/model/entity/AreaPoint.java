package com.pw.localizer.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

@Entity
public class AreaPoint {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
	private long id;

	@NotNull
	private int number;

	@NotNull
	private double lat;

	@NotNull
	private double lng;

	public AreaPoint(int number, double lat, double lng) {
		this.number = number;
		this.lat = lat;
		this.lng = lng;
	}

	public AreaPoint() {}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
}
