package com.pw.localizer.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class AreaEvent implements Serializable{
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Id
	private long id;

    @ManyToOne
    private Area area;
    
    @NotNull
    private String message;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private boolean mailSend;

    private String url;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isMailSend() {
		return mailSend;
	}

	public void setMailSend(boolean mailSend) {
		this.mailSend = mailSend;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public abstract Location getLocation();
}
