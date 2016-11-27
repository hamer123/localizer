package com.pw.localizer.model.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property="type")
@JsonSubTypes({
		@JsonSubTypes.Type(name = "gsm", value = CellInfoGSM.class),
		@JsonSubTypes.Type(name = "lte", value = CellInfoLte.class)
})
public abstract class CellInfoMobile implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
    private long id;
    
	@Embedded
	private SignalStrength signalStrength;
}
