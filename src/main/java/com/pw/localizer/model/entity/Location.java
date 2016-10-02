package com.pw.localizer.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pw.localizer.model.enums.Provider;
import org.jboss.resteasy.links.RESTServiceDiscovery;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Location implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
    private Long id;

	@NotNull
	private double latitude;

	@NotNull
	private double longitude;

	@JsonIgnore
	@ManyToOne
	private User user;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date date;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(updatable = false)
	private Provider providerType;

	@Embedded
	private Address address;

	@NotNull
	@Column(updatable = false)
	private double accuracy;

	@XmlTransient
	private boolean eventCheck;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Provider getProviderType() {
		return providerType;
	}

	public void setProviderType(Provider providerType) {
		this.providerType = providerType;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public boolean isEventCheck() {
		return eventCheck;
	}

	public void setEventCheck(boolean eventCheck) {
		this.eventCheck = eventCheck;
	}

}

	
