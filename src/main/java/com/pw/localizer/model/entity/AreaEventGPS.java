package com.pw.localizer.model.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries(value ={
		@NamedQuery(name = "AreaEventGPS.findByAreaId", 
				    query = "SELECT a FROM AreaEventGPS a WHERE a.area.id = :id"),
		@NamedQuery(name = "AreaEventGPS.findAllWhereMailSendIsTrue", 
		            query = "SELECT a FROM AreaEventGPS a WHERE a.sendMail = true"),
		@NamedQuery(name = "AreaEventGPS.findByAreaIdAndDate",
		            query = "SELECT a FROM AreaEventGPS a WHERE a.area.id =:id AND a.date > :from"),
		@NamedQuery(name = "AreaEventGPS.findBySendMailAndAttemptToSend",
		            query = "SELECT a FROM AreaEventGPS a WHERE a.sendMail =:sendMail AND a.attemptToSend <:attemptToSend")
})
public class AreaEventGPS extends AreaEvent {

	@OneToOne
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

	public void setLocationGPS(LocationGPS locationGPS) {
		this.locationGPS = locationGPS;
	}
}
