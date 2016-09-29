package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.AreaPoint;
import com.pw.localizer.model.enums.AreaFollow;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Patryk on 2016-09-26.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaDTO {
    private long id;
    private UserDTO userDTOTarget;
    private String name;
    private boolean active;
    private String color;
    private AreaFollow polygonFollowType;
    private List<AreaPoint> points;

    public static AreaDTO convertToDTO(Area area){
        AreaDTO areaDTO = new AreaDTO();
        areaDTO.id = area.getId();
        areaDTO.name = area.getName();
        areaDTO.active = area.isActive();
        areaDTO.color = area.getColor();
        areaDTO.points = area.getPoints();
        areaDTO.polygonFollowType = area.getAreaFollowType();
        //lazy initial
        area.getTarget().setAreas(new HashSet());
        areaDTO.userDTOTarget = UserDTO.convertToDto(area.getTarget());
        return areaDTO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDTO getUserDTOTarget() {
        return userDTOTarget;
    }

    public void setUserDTOTarget(UserDTO userDTOTarget) {
        this.userDTOTarget = userDTOTarget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public AreaFollow getPolygonFollowType() {
        return polygonFollowType;
    }

    public void setPolygonFollowType(AreaFollow polygonFollowType) {
        this.polygonFollowType = polygonFollowType;
    }

    public List<AreaPoint> getPoints() {
        return points;
    }

    public void setPoints(List<AreaPoint> points) {
        this.points = points;
    }
}
