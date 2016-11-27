package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pw.localizer.model.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private long id;
    private String login;
    private String email;
    private String phone;
    private Set<AreaDTO> areas;
    private UserSetting userSetting;
    private UserLastLocationsDTO userLastLocations;
    private Avatar avatar;
}
