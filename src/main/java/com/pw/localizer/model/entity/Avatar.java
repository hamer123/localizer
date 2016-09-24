package com.pw.localizer.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import com.pw.localizer.model.enums.ImageType;

@Entity
public class Avatar implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private long id;

	@NotNull
	private ImageType format;

	@NotNull
	private String uuid;

	@NotNull
	private String name;

	@NotNull
	private long size;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public ImageType getFormat() {
		return format;
	}
	public void setFormat(ImageType format) {
		this.format = format;
	}

}
