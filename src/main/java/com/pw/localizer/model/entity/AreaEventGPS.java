package com.pw.localizer.model.entity;

import javax.persistence.*;

@NamedQueries(value ={
		@NamedQuery(name = "AreaEventGPS.findByAreaId",
				query = "SELECT a FROM AreaEventGPS a WHERE a.area.id = :id"),
		@NamedQuery(name = "AreaEventGPS.findAllWhereMailSendIsTrue",
				query = "SELECT a FROM AreaEventGPS a WHERE a.sendMail = true"),
		@NamedQuery(name = "AreaEventGPS.findByAreaIdAndDate",
				query = "SELECT a FROM AreaEventGPS a WHERE a.area.id =:id AND a.date > :from"),
		@NamedQuery(name = "AreaEventGPS.findBySendMailAndAttemptToSend",
				query = "SELECT a FROM AreaEventGPS a WHERE a.sendMail =:sendMail AND a.attemptToSend <:attemptToSend"),
		@NamedQuery(name = "AreaEventGPS.deleteByArea",
				query = "DELETE FROM AreaEventGPS aeg WHERE aeg.area.id =:areaId")
})
@Entity
public class AreaEventGPS extends AreaEvent {

	@OneToOne
	@JoinColumn
	private LocationGPS locationGPS;

	public AreaEventGPS() {
	}

	public AreaEventGPS(LocationGPS locationGPS){
		this.locationGPS = locationGPS;
	}

	@Override
	public Location getLocation() {
		return locationGPS;
	}
}
