package com.pw.localizer.model.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import com.pw.localizer.model.enums.ImageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Avatar implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private long id;

	@NotNull
	private ImageType format;

	@NotNull
	private String uuid;

	@NotNull
	private String name;

	@NotNull
	private long size;
}
