package com.pw.localizer.model.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property="type")
@JsonSubTypes({
		@JsonSubTypes.Type(name = "cdma", value = SignalStrengthCdma.class),
		@JsonSubTypes.Type(name = "basic", value = SignalStrength.class)
})
public class SignalStrength implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;

	private int assusLevel;
	private int dbm;
	private int level;
}
