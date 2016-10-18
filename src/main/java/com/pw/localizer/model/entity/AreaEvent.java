package com.pw.localizer.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

	@Max(3) @Min(0)
	private int attemptToSend;

	private String accessToken;

    private boolean sendMail;

	@PrePersist
	public void prePersist(){
		this.accessToken = UUID.randomUUID().toString();
	}

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

	public boolean isSendMail() {
		return sendMail;
	}

	public void setSendMail(boolean mailSend) {
		this.sendMail = mailSend;
	}

	public abstract Location getLocation();

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

    public int getAttemptToSend() {
        return attemptToSend;
    }

    public void setAttemptToSend(int attemptToSend) {
        this.attemptToSend = attemptToSend;
    }
}
