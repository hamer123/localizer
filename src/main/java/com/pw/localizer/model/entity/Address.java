package com.pw.localizer.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Getter
@Setter
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class Address implements Serializable{
	private String city;

	private String street;
}
