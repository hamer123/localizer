package com.pw.localizer.model.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.pw.localizer.model.enums.AreaMailMessageMode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
public class AreaMessageMail implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;

    private boolean active;

    private boolean accept = true;

	@NotNull
    @Enumerated(EnumType.STRING)
    private AreaMailMessageMode areaMailMessageMode;
}
