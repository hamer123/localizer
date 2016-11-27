package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pw.localizer.model.entity.*;

import java.util.Set;

/**
 * Created by Patryk on 2016-09-26.
 */

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public UserLastLocationsDTO getUserLastLocations() {
        return userLastLocations;
    }

    public void setUserLastLocations(UserLastLocationsDTO userLastLocations) {
        this.userLastLocations = userLastLocations;
    }

    public Set<AreaDTO> getAreas() {
        return areas;
    }

    public void setAreas(Set<AreaDTO> areas) {
        this.areas = areas;
    }

    public UserSetting getUserSetting() {
        return userSetting;
    }

    public void setUserSetting(UserSetting userSetting) {
        this.userSetting = userSetting;
    }

}
