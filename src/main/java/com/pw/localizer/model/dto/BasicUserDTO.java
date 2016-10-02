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

    public static BasicUserDTO convertToBasicUserDTO(User user){
        BasicUserDTO basicUserDTO = new BasicUserDTO();
        basicUserDTO.id = user.getId();
        basicUserDTO.login = user.getLogin();
        basicUserDTO.email = user.getEmail();
        basicUserDTO.phone = user.getPhone();
        return basicUserDTO;
    }

    public static User convertToUser(BasicUserDTO basicUserDTO){
        User user = new User();
        user.setId(basicUserDTO.getId());
        user.setLogin(basicUserDTO.getLogin());
        user.setEmail(basicUserDTO.getEmail());
        user.setPhone(basicUserDTO.getPhone());
        return user;
    }

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
