package com.pw.localizer.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Embeddable
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SignalStrength implements Serializable{
	@NotNull
	private int assusLevel;

	@NotNull
	private int dbm;

	@NotNull
	private int level;
	
	public int getAssusLevel() {
		return assusLevel;
	}
	public void setAssusLevel(int assusLevel) {
		this.assusLevel = assusLevel;
	}
	public int getDbm() {
		return dbm;
	}
	public void setDbm(int dbm) {
		this.dbm = dbm;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

}
