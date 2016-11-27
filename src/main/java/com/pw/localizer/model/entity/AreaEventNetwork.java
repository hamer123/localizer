package com.pw.localizer.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@NamedQueries(value ={
		@NamedQuery(name  = "AreaEventNetwork.findByAreaId",
				query = "SELECT a FROM AreaEventNetwork a WHERE a.area.id = :id"),
		@NamedQuery(name  = "AreaEventNetwork.findAllWhereMailSendIsTrue",
				query = "SELECT a FROM AreaEventNetwork a WHERE a.sendMail = true"),
		@NamedQuery(name  = "AreaEventNetwork.findAll",
				query = "SELECT a FROM AreaEventNetwork a"),
		@NamedQuery(name  = "AreaEventNetwork.removeById",
				query = "DELETE FROM AreaEventNetwork a WHERE a.id =:id"),
		@NamedQuery(name  = "AreaEventNetwork.findByAreaIdAndDate",
				query = "SELECT a FROM AreaEventNetwork a WHERE a.area.id =:id AND a.date > :from"),
		@NamedQuery(name = "AreaEventNetwork.findBySendMailAndAttemptToSendLowerThan",
				query = "SELECT a FROM AreaEventNetwork a WHERE a.sendMail =:sendMail AND a.attemptToSend <:attemptToSend"),
		@NamedQuery(name = "AreaEventNetwork.deleteByArea",
				query = "DELETE FROM AreaEventNetwork aen WHERE aen.area.id =:areaId"),
		@NamedQuery(name = "AreaEventNetwork.deleteByAreaAndLocalizerService",
				query = "DELETE FROM AreaEventNetwork aen WHERE aen.area.id =:areaId AND aen.locationNetwork.localizationService =:localizerService")
})
@Entity
@Getter
@Setter
public class AreaEventNetwork extends AreaEvent {

	@OneToOne
	@JoinColumn
    private LocationNetwork locationNetwork;

	public AreaEventNetwork() {
	}

    public AreaEventNetwork(LocationNetwork locationNetwork){
    	this.locationNetwork = locationNetwork;
    }

	@Override
	public Location getLocation() {
		return locationNetwork;
	}
}
