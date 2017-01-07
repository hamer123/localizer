package com.pw.localizer.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pw.localizer.model.enums.Provider;
import lombok.Getter;
import lombok.Setter;
import org.jboss.resteasy.links.RESTServiceDiscovery;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Location implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
    private Long id;

	@NotNull
	private double latitude;

	@NotNull
	private double longitude;

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

	@Column(updatable = false)
	private double accuracy;

	@Column(updatable = false)
	private float bearing;

//    @Column(nullable = true)
    private String networkType;

	private boolean eventCheck;
}
