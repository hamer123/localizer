package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pw.localizer.model.entity.Avatar;
import com.pw.localizer.model.entity.User;

/**
 * Created by Patryk on 2016-09-29.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicUserDTO {
    protected long id;
    protected String login;
    protected String email;
    protected String phone;

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

}
