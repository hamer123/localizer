package com.pw.localizer.model.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property="type")
@JsonSubTypes({
		@JsonSubTypes.Type(name = "gsm", value = CellInfoGSM.class),
		@JsonSubTypes.Type(name = "lte", value = CellInfoLte.class)
})
public abstract class CellInfoMobile implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
    private long id;
    
	@OneToOne(orphanRemoval = true, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private SignalStrength signalStrength;
}
