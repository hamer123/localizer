package com.pw.localizer.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import com.pw.localizer.model.enums.AreaMailMessageMode;

@Entity
public class AreaMessageMail {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    @Column(name = "id")
	private long id;

    private boolean active;

    private boolean accept = true;

	@NotNull
    @Enumerated(EnumType.STRING)
    private AreaMailMessageMode areaMailMessageMode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}

	public AreaMailMessageMode getAreaMailMessageMode() {
		return areaMailMessageMode;
	}

	public void setAreaMailMessageMode(AreaMailMessageMode areaMailMessageMode) {
		this.areaMailMessageMode = areaMailMessageMode;
	}


}
