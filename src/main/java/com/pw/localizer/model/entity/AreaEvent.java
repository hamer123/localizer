package com.pw.localizer.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NamedQueries({
		@NamedQuery(name = "AreaEvent.removeByArea",
				query = "DELETE FROM AreaEvent ae WHERE ae.area.id =:areaId"),
		@NamedQuery(name = "AreaEvent.findAll",
				query = "SELECT ae FROM AreaEvent ae"),
		@NamedQuery(name = "AreaEvent.findByAreaAndYoungerAndOlder",
				query = "SELECT ae FROM AreaEvent ae WHERE ae.date > :fromDate AND ae.date < :toDate AND ae.area.id = :areaId ORDER BY ae.date ASC"),
		@NamedQuery(name = "AreaEvent.findByAreaAndDateOlder",
				query = "SELECT ae FROM AreaEvent ae WHERE ae.area.id = :areaId AND ae.date > :fromDate"),
		@NamedQuery(name = "AreaEvent.findBySendMailAndAttemptToSendLowerThan",
				query = "SELECT ae FROM AreaEvent ae WHERE ae.sendMail = :sendMail AND ae.attemptToSend < :attemptToSend"),
		@NamedQuery(name = "AreaEvent.findByAreaAndType",
				query = "SELECT ae FROM AreaEvent ae WHERE TYPE(ae) IN (:types) AND ae.area.id = :areaId"),
		@NamedQuery(name = "AreaEvent.findByAreaAndLocalizationService",
				query = "SELECT ae FROM AreaEventNetwork ae WHERE ae.area.id = :areaId AND ae.locationNetwork.localizationService =:localizationService")
})
@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AreaEvent implements Serializable{
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Id
	private long id;
    
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

	public abstract Location getLocation();

	@ManyToOne
	@JoinColumn
	private Area area;
}
