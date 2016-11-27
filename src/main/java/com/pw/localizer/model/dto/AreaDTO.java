package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pw.localizer.model.entity.AreaPoint;
import com.pw.localizer.model.enums.AreaFollow;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaDTO {
    private long id;
    private BasicUserDTO target;
    private String name;
    private boolean active;
    private String color;
    private AreaFollow polygonFollowType;
    private List<AreaPoint> points;
}
